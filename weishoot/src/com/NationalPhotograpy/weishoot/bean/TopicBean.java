
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

public class TopicBean implements Serializable {
    public ResultStatus result;

    public List<TopicData> data;

    public class TopicData implements Serializable {

        public TopicData() {
            super();
        }

        public TopicData(List<TopPhotoBean> photoList, String tCode, String title, String mark,
                String createDate, String createFrom, String tType, String picAddress,
                String coordinate, String uCode, String userHead, String nickName,
                int commentCount, int goodCount, String picSort, String isAttention,
                String isCollection, String isGood) {
            super();
            PhotoList = photoList;
            TCode = tCode;
            Title = title;
            Mark = mark;
            CreateDate = createDate;
            CreateFrom = createFrom;
            TType = tType;
            PicAddress = picAddress;
            Coordinate = coordinate;
            UCode = uCode;
            UserHead = userHead;
            NickName = nickName;
            CommentCount = commentCount;
            GoodCount = goodCount;
            PicSort = picSort;
            IsAttention = isAttention;
            IsCollection = isCollection;
            IsGood = isGood;
        }

        public List<TopPhotoBean> PhotoList;

        public List<TopCommentBean> CommentList;

        public List<TopGoodBean> GoodList;

        public String TCode;

        public String Title;

        public String Mark;

        public String CreateDate;

        public String CreateFrom;

        public String TType;

        public String PicAddress;

        public String Coordinate;

        public String UCode;

        public String UserHead;

        public String NickName;

        public int CommentCount;

        public int GoodCount;

        public String PicSort;

        public String IsAttention;

        public String IsCollection;

        public String IsGood;
    }
}
