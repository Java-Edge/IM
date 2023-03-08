package com.javaedge.im.common.constant;

/**
 * @author JavaEdge
 */
public interface Constants {

    /**
     * channel绑定的userId Key
     */
    String UserId = "userId";

    /**
     * channel绑定的appId
     */
    String AppId = "appId";

    String ClientType = "clientType";

    String Imei = "imei";

    /**
     * channel绑定的clientType 和 imel Key
     */
    String ClientImei = "clientImei";

    String ReadTime = "readTime";

    String ImCoreZkRoot = "/im-coreRoot";

    String ImCoreZkRootTcp = "/tcp";

    String ImCoreZkRootWeb = "/web";


    interface RedisConstants {

        /**
         * userSign，格式：appId:userSign:
         */
        String userSign = "userSign";

        /**
         * 用户上线通知channel
         */
        String UserLoginChannel
                = "signal/channel/LOGIN_USER_INNER_QUEUE";


        /**
         * 用户session，appId + UserSessionConstants + 用户id 例如10000：userSession：javaedge
         */
        String UserSessionConstants = ":userSession:";

        /**
         * 缓存客户端消息防重，格式： appId + :cacheMessage: + messageId
         */
        String cacheMessage = "cacheMessage";

        String OfflineMessage = "offlineMessage";

        /**
         * seq 前缀
         */
        String SeqPrefix = "seq";

        /**
         * 用户订阅列表，格式 ：appId + :subscribe: + userId。Hash结构，filed为订阅自己的人
         */
        String subscribe = "subscribe";

        /**
         * 用户自定义在线状态，格式 ：appId + :userCustomerStatus: + userId。set，value为用户id
         */
        String userCustomerStatus = "userCustomerStatus";

    }

    interface RabbitConstants {

        String Im2UserService = "pipeline2UserService";

        String Im2MessageService = "pipeline2MessageService";

        String Im2GroupService = "pipeline2GroupService";

        String Im2FriendshipService = "pipeline2FriendshipService";

        String MessageService2Im = "messageService2Pipeline";

        String GroupService2Im = "GroupService2Pipeline";

        String FriendShip2Im = "friendShip2Pipeline";

        String StoreP2PMessage = "storeP2PMessage";

        String StoreGroupMessage = "storeGroupMessage";


    }

    interface CallbackCommand {
        String ModifyUserAfter = "user.modify.after";

        String CreateGroupAfter = "group.create.after";

        String UpdateGroupAfter = "group.update.after";

        String DestroyGroupAfter = "group.destroy.after";

        String TransferGroupAfter = "group.transfer.after";

        String GroupMemberAddBefore = "group.member.add.before";

        String GroupMemberAddAfter = "group.member.add.after";

        String GroupMemberDeleteAfter = "group.member.delete.after";

        String AddFriendBefore = "friend.add.before";

        String AddFriendAfter = "friend.add.after";

        String UpdateFriendBefore = "friend.update.before";

        String UpdateFriendAfter = "friend.update.after";

        String DeleteFriendAfter = "friend.delete.after";

        String AddBlackAfter = "black.add.after";

        String DeleteBlack = "black.delete";

        String SendMessageAfter = "message.send.after";

        String SendMessageBefore = "message.send.before";

    }

    interface SeqConstants {
        String Message = "messageSeq";

        String GroupMessage = "groupMessageSeq";


        String Friendship = "friendshipSeq";

//    String FriendshipBlack = "friendshipBlackSeq";

        String FriendshipRequest = "friendshipRequestSeq";

        String FriendshipGroup = "friendshipGrouptSeq";

        String Group = "groupSeq";

        String Conversation = "conversationSeq";

    }

}
