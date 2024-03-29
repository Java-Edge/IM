package com.javaedge.im.service.conversation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.javaedge.im.codec.pack.conversation.DeleteConversationPack;
import com.javaedge.im.codec.pack.conversation.UpdateConversationPack;
import com.javaedge.im.common.ResponseVO;
import com.javaedge.im.common.config.AppConfig;
import com.javaedge.im.common.constant.Constants;
import com.javaedge.im.common.enums.ConversationErrorCode;
import com.javaedge.im.common.enums.ConversationTypeEnum;
import com.javaedge.im.common.enums.command.ConversationEventCommand;
import com.javaedge.im.common.model.ClientInfo;
import com.javaedge.im.common.model.SyncReq;
import com.javaedge.im.common.model.SyncResp;
import com.javaedge.im.common.model.message.MessageReadedContent;
import com.javaedge.im.service.conversation.dao.ImConversationSetEntity;
import com.javaedge.im.service.conversation.dao.mapper.ImConversationSetMapper;
import com.javaedge.im.service.conversation.model.DeleteConversationReq;
import com.javaedge.im.service.conversation.model.UpdateConversationReq;
import com.javaedge.im.service.seq.RedisSeq;
import com.javaedge.im.service.utils.MsgProducer;
import com.javaedge.im.service.utils.WriteUserSeq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
 */
@Service
public class ConversationService {

    @Autowired
    ImConversationSetMapper imConversationSetMapper;

    @Autowired
    MsgProducer msgProducer;

    @Autowired
    AppConfig appConfig;

    @Autowired
    RedisSeq redisSeq;

    @Autowired
    WriteUserSeq writeUserSeq;

    public String convertConversationId(Integer type,String fromId,String toId){
        return type + "_" + fromId + "_" + toId;
    }

    public void  messageMarkRead(MessageReadedContent messageReadedContent){

        String toId = messageReadedContent.getToId();
        if(messageReadedContent.getConversationType() == ConversationTypeEnum.GROUP.getCode()){
            toId = messageReadedContent.getGroupId();
        }
        String conversationId = convertConversationId(messageReadedContent.getConversationType(),
                messageReadedContent.getFromId(), toId);
        QueryWrapper<ImConversationSetEntity> query = new QueryWrapper<>();
        query.eq("conversation_id",conversationId);
        query.eq("app_id",messageReadedContent.getAppId());
        ImConversationSetEntity imConversationSetEntity = imConversationSetMapper.selectOne(query);
        if(imConversationSetEntity == null){
            imConversationSetEntity = new ImConversationSetEntity();
            long seq = redisSeq.doGetSeq(messageReadedContent.getAppId() + ":" + Constants.SeqConstants.Conversation);
            imConversationSetEntity.setConversationId(conversationId);
            BeanUtils.copyProperties(messageReadedContent,imConversationSetEntity);
            imConversationSetEntity.setReadedSequence(messageReadedContent.getMessageSequence());
            imConversationSetEntity.setToId(toId);
            imConversationSetEntity.setSequence(seq);
            imConversationSetMapper.insert(imConversationSetEntity);
            writeUserSeq.writeUserSeq(messageReadedContent.getAppId(),
                    messageReadedContent.getFromId(),Constants.SeqConstants.Conversation,seq);
        }else{
            long seq = redisSeq.doGetSeq(messageReadedContent.getAppId() + ":" + Constants.SeqConstants.Conversation);
            imConversationSetEntity.setSequence(seq);
            imConversationSetEntity.setReadedSequence(messageReadedContent.getMessageSequence());
            imConversationSetMapper.readMark(imConversationSetEntity);
            writeUserSeq.writeUserSeq(messageReadedContent.getAppId(),
                    messageReadedContent.getFromId(),Constants.SeqConstants.Conversation,seq);
        }
    }

    /**
     * @description: 删除会话
     * @param
     * @return com.javaedge.im.common.ResponseVO
     * @author javaedge
     */
    public ResponseVO deleteConversation(DeleteConversationReq req){

        //置顶 有免打扰
//        QueryWrapper<ImConversationSetEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("conversation_id",req.getConversationId());
//        queryWrapper.eq("app_id",req.getAppId());
//        ImConversationSetEntity imConversationSetEntity = imConversationSetMapper.selectOne(queryWrapper);
//        if(imConversationSetEntity != null){
//            imConversationSetEntity.setIsMute(0);
//            imConversationSetEntity.setIsTop(0);
//            imConversationSetMapper.update(imConversationSetEntity,queryWrapper);
//        }

        if(appConfig.getDeleteConversationSyncMode() == 1){
            DeleteConversationPack pack = new DeleteConversationPack();
            pack.setConversationId(req.getConversationId());
            msgProducer.send2UserExceptClient(req.getFromId(),
                    ConversationEventCommand.CONVERSATION_DELETE,
                    pack,new ClientInfo(req.getAppId(),req.getClientType(),
                            req.getImei()));
        }
        return ResponseVO.successResponse();
    }

    /**
     * @description: 更新会话 置顶or免打扰
     * @param
     * @return com.javaedge.im.common.ResponseVO
     * @author javaedge
     */
    public ResponseVO updateConversation(UpdateConversationReq req){




        if(req.getIsTop() == null && req.getIsMute() == null){
            return ResponseVO.errorResponse(ConversationErrorCode.CONVERSATION_UPDATE_PARAM_ERROR);
        }
        QueryWrapper<ImConversationSetEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id",req.getConversationId());
        queryWrapper.eq("app_id",req.getAppId());
        ImConversationSetEntity imConversationSetEntity = imConversationSetMapper.selectOne(queryWrapper);
        if(imConversationSetEntity != null){
            long seq = redisSeq.doGetSeq(req.getAppId() + ":" + Constants.SeqConstants.Conversation);

            if(req.getIsMute() != null){
                imConversationSetEntity.setIsTop(req.getIsTop());
            }
            if(req.getIsMute() != null){
                imConversationSetEntity.setIsMute(req.getIsMute());
            }
            imConversationSetEntity.setSequence(seq);
            imConversationSetMapper.update(imConversationSetEntity,queryWrapper);
            writeUserSeq.writeUserSeq(req.getAppId(), req.getFromId(),
                    Constants.SeqConstants.Conversation, seq);

            UpdateConversationPack pack = new UpdateConversationPack();
            pack.setConversationId(req.getConversationId());
            pack.setIsMute(imConversationSetEntity.getIsMute());
            pack.setIsTop(imConversationSetEntity.getIsTop());
            pack.setSequence(seq);
            pack.setConversationType(imConversationSetEntity.getConversationType());
            msgProducer.send2UserExceptClient(req.getFromId(),
                    ConversationEventCommand.CONVERSATION_UPDATE,
                    pack,new ClientInfo(req.getAppId(),req.getClientType(),
                            req.getImei()));
        }
        return ResponseVO.successResponse();
    }

    public ResponseVO syncConversationSet(SyncReq req) {
        if(req.getMaxLimit() > 100){
            req.setMaxLimit(100);
        }

        SyncResp<ImConversationSetEntity> resp = new SyncResp<>();
        //seq > req.getseq limit maxLimit
        QueryWrapper<ImConversationSetEntity> queryWrapper =
                new QueryWrapper<>();
        queryWrapper.eq("from_id",req.getOperater());
        queryWrapper.gt("sequence",req.getLastSequence());
        queryWrapper.eq("app_id",req.getAppId());
        queryWrapper.last(" limit " + req.getMaxLimit());
        queryWrapper.orderByAsc("sequence");
        List<ImConversationSetEntity> list = imConversationSetMapper
                .selectList(queryWrapper);

        if(!CollectionUtils.isEmpty(list)){
            ImConversationSetEntity maxSeqEntity = list.get(list.size() - 1);
            resp.setDataList(list);
            //设置最大seq
            Long friendShipMaxSeq = imConversationSetMapper.geConversationSetMaxSeq(req.getAppId(), req.getOperater());
            resp.setMaxSequence(friendShipMaxSeq);
            //设置是否拉取完毕
            resp.setCompleted(maxSeqEntity.getSequence() >= friendShipMaxSeq);
            return ResponseVO.successResponse(resp);
        }

        resp.setCompleted(true);
        return ResponseVO.successResponse(resp);

    }
}
