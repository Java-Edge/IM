package com.javaedge.im.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.javaedge.im.codec.pack.group.AddGroupMemberPack;
import com.javaedge.im.codec.pack.group.RemoveGroupMemberPack;
import com.javaedge.im.codec.pack.group.UpdateGroupMemberPack;
import com.javaedge.im.common.ClientType;
import com.javaedge.im.common.enums.command.Command;
import com.javaedge.im.common.enums.command.GroupEventCommand;
import com.javaedge.im.common.model.ClientInfo;
import com.javaedge.im.service.group.model.req.GroupMemberDto;
import com.javaedge.im.service.group.service.ImGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
 */
@Component
public class GroupMessageProducer {

    @Autowired
    MsgProducer msgProducer;

    @Autowired
    ImGroupMemberService imGroupMemberService;

    public void producer(String userId, Command command, Object data,
                         ClientInfo clientInfo){
        JSONObject o = (JSONObject) JSONObject.toJSON(data);
        String groupId = o.getString("groupId");
        List<String> groupMemberId = imGroupMemberService
                .getGroupMemberId(groupId, clientInfo.getAppId());

        if(command.equals(GroupEventCommand.ADDED_MEMBER)){
            //发送给管理员和被加入人本身
            List<GroupMemberDto> groupManager = imGroupMemberService.getGroupManager(groupId, clientInfo.getAppId());
            AddGroupMemberPack addGroupMemberPack
                    = o.toJavaObject(AddGroupMemberPack.class);
            List<String> members = addGroupMemberPack.getMembers();
            for (GroupMemberDto groupMemberDto : groupManager) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && groupMemberDto.getMemberId().equals(userId)){
                    msgProducer.send2UserExceptClient(groupMemberDto.getMemberId(),command,data,clientInfo);
                }else{
                    msgProducer.send2User(groupMemberDto.getMemberId(),command,data,clientInfo.getAppId());
                }
            }
            for (String member : members) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && member.equals(userId)){
                    msgProducer.send2UserExceptClient(member,command,data,clientInfo);
                }else{
                    msgProducer.send2User(member,command,data,clientInfo.getAppId());
                }
            }
        }else if(command.equals(GroupEventCommand.DELETED_MEMBER)){
            RemoveGroupMemberPack pack = o.toJavaObject(RemoveGroupMemberPack.class);
            String member = pack.getMember();
            List<String> members = imGroupMemberService.getGroupMemberId(groupId, clientInfo.getAppId());
            members.add(member);
            for (String memberId : members) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && member.equals(userId)){
                    msgProducer.send2UserExceptClient(memberId,command,data,clientInfo);
                }else{
                    msgProducer.send2User(memberId,command,data,clientInfo.getAppId());
                }
            }
        }else if(command.equals(GroupEventCommand.UPDATED_MEMBER)){
            UpdateGroupMemberPack pack =
                    o.toJavaObject(UpdateGroupMemberPack.class);
            String memberId = pack.getMemberId();
            List<GroupMemberDto> groupManager = imGroupMemberService.getGroupManager(groupId, clientInfo.getAppId());
            GroupMemberDto groupMemberDto = new GroupMemberDto();
            groupMemberDto.setMemberId(memberId);
            groupManager.add(groupMemberDto);
            for (GroupMemberDto member : groupManager) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && member.equals(userId)){
                    msgProducer.send2UserExceptClient(member.getMemberId(),command,data,clientInfo);
                }else{
                    msgProducer.send2User(member.getMemberId(),command,data,clientInfo.getAppId());
                }
            }
        }else {
            for (String memberId : groupMemberId) {
                if(clientInfo.getClientType() != null && clientInfo.getClientType() !=
                        ClientType.WEBAPI.getCode() && memberId.equals(userId)){
                    msgProducer.send2UserExceptClient(memberId,command,
                            data,clientInfo);
                }else{
                    msgProducer.send2User(memberId,command,data,clientInfo.getAppId());
                }
            }
        }



    }

}
