
package com.NationalPhotograpy.weishoot.net;

public class HttpUrl {

//    public static final String url_base = "http://openapi.weishoot.com";
    
    public static final String url_base="http://115.28.91.139:8088";

    // Android授权码
    public static final String tokenkey = "FE4311EB-330B-4007-BAFD-6A844F4D21C7";
    
//    public static final String tokenkey="B61A9DA5-8981-451F-9155-42ABE7A8D8BB";

    // 注册协议
    public static final String AgreementRegister = "http://weishoot.com/agreement/reg.html";

    // 销售协议
    public static final String AgreementSale = "http://weishoot.com/agreement/sales.html";

    // 获取融云Token
    public static final String GetToken = "http://chatapi.weishoot.com/ApiPortal.asmx/GetToken";

    public static final String basePic_url = "http://weishoot.com/Thumbnails.asp?p=";

    // 获取主题
    public static final String GetTopic = url_base + "/Topic/GetTopic/";

    public static final String url_BDTranslation = "http://openapi.baidu.com/public/2.0/bmt/translate";

    // 获取评论
    public static final String GetCommentNum = url_base + "/Comment/TotalCommentCount";

    // 获取点赞
    public static final String GetGoodNum = url_base + "/Good/TotalGoodCount";

    // 获取所有喜好风格
    public static final String GetAllTag = url_base + "/Tag/GetAllTag";

    // 获取用户信息
    public static final String GetUserInfo = url_base + "/User/GetUsersByCode/";

    // 注册新用户
    public static final String UserRegist = url_base + "/User/Regist";

    // 修改用户信息
    public static final String UpdateUser = url_base + "/User/UpdateUser";

    // 修改头像
    public static final String ModifyAvatar = url_base + "/User/ModifyAvatar";

    // 修改用户顶图
    public static final String ReceiveTopCover = url_base + "/User/ReceiveTopCover";

    // 登录
    public static final String CheckLoin = url_base + "/User/CheckLoin";

    // 根据登录名获取用户信息用于找回密码
    public static final String GetUserByLoginName = url_base + "/User/GetUserByLoginName";

    // 获取图店分类
    public static final String GetPhotoShopType = url_base + "/Photo/GetPhotoShopType";

    // 根据图片编号获取图片扩展信息
    public static final String GetExifByCode = url_base + "/Photo/GetExifByCode";

    // 忘记密码修改密码接口
    public static final String ForgotPassword = url_base + "/User/ForgotPassword";

    // 获取图店分类更多或图片墙
    public static final String GetPhotosAll = url_base + "/Photo/GetPhotosAll/";

    // 获取广告
    public static final String GetAd = url_base + "/ADitem/GetAd";

    // 发微摄
    public static final String AddTopic = url_base + "/Topic/AddTopic/";

    // 制作影片
    public static final String AddFilm = url_base + "/Topic/AddFilm/";

    // 制作封面
    public static final String AddCover = url_base + "/Topic/AddCover/";

    // 根据选中的图片数量获取图片布局
    public static final String GetPicSort = url_base + "/Topic/GetPicSort/";

    // 获取收藏主题
    public static final String GetCollections = url_base + "/Topic/GetCollections/";

    // 点赞操作
    public static final String ManageGood = url_base + "/Good/ManageGood";

    // 获取评论
    public static final String AddComment = url_base + "/Comment/AddComment";

    // 根据编号获取评论.
    public static final String GetComment = url_base + "/Comment/GetComment";

    // 删除评论
    public static final String DeleteComment = url_base + "/Comment/DeleteComment";

    // 添加或删除收藏主题
    public static final String ManageCollections = url_base + "/Topic/ManageCollections/";

    // 添加反馈，举报
    public static final String AddErrorBack = url_base + "/User/AddErrorBack";

    // 删除主题
    public static final String DeleteTopic = url_base + "/Topic/DeleteTopic";

    // 搜索用户
    public static final String SerchGetAllUsers = url_base + "/User/SerchGetAllUsers";

    // 搜索主题
    public static final String GetTopicSerch = url_base + "/Topic/GetTopicSerch/";

    // 根据被评论的主题编号获取点赞人的信息
    public static final String GetGoodUsers = url_base + "/Good/GetGoodUsers";

    // 执行关注和删除关注
    public static final String AttentionOption = url_base + "/Attention/AttentionOption";

    // 获取粉丝
    public static final String GetUsersByUCode = url_base + "/Attention/GetUsersByUCode";

    // 获取关注列表
    public static final String GetUserAttentionByUCode = url_base
            + "/Attention/GetUserAttentionByUCode";

    // 获取推荐用户
    public static final String GetRecommendedUsers = url_base + "/User/GetRecommendedUsers/";

    // 热门搜索
    public static final String GetHotSearch = url_base + "/User/GetHotSearch";

    // 获取相互关注的好友
    public static final String GetFriendByCode = url_base + "/Attention/GetFriendByCode";

    // 更新用户图贝
    public static final String UpdateCoin = url_base + "/User/UpdateCoin";

    // 获取用户的图贝和单张图片的费用
    public static final String GetCoinAndFee = url_base + "/User/GetCoinAndFee";

    // 购买图片
    public static final String BuyPhoto = url_base + "/Photo/BuyPhoto";

    // 检查第三方是否绑定过
    public static final String ChkThirdUCode = url_base + "/User/ChkThirdUCode";

    // 获取相互关注的好友
    public static final String GetOneWayAttention = url_base + "/Attention/GetOneWayAttention";

    // 第三方用户绑定
    public static final String BindUser = url_base + "/User/BindUser";

    // 密码解密
    public static final String DecryptPassword = url_base + "/User/DecryptPassword";

    // 获取封面分类
    public static final String GetCoverClass = url_base + "/Cover/GetCoverClass";

    // 获取所有行业
    public static final String GetOccupations = url_base + "/Occupation/GetOccupations";

    // 添加用户扩展信息
    public static final String UpdateUserInfo = url_base + "/User/UpdateUserInfo";

    // 根据分类编号获取封面
    public static final String GetCoverPicByClass = url_base + "/Cover/GetCoverPicByClass";

    // 根据用户编号获取用户喜欢风格
    public static final String GeTagsByUCode = url_base + "/User/GeTagsByUCode";

    // 添加用户标签
    public static final String AddUserTags = url_base + "/User/AddUserTags";

    // 版本检测升级
    public static final String GetNewVersion = url_base + "/Home/GetNewVersion";

    // 监测手机号是否被注册
    public static final String ChkTelephone = url_base + "/User/ChkTelephone";
    
    public static final String GetBanner=url_base+ "/Photo/GetRCMDBannerInfo";
    
    public static final String GetRCMDUserInfo=url_base+"/Photo/GetRCMDUserInfo";
    
    public static final String GetRCMDPhotoInfo=url_base+"/Photo/GetRCMDPhotoInfo";
    
    public static final String GetTopicByTCode=url_base+"/Topic/GetTopicByTCode";
    
}
