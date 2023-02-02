package com.clone.ohouse.document;

public class CommunityApiDescription {
    public static final String description =
            "<h3>Community API</h3>" + "<br>" +
                    "커뮤니티 - 사진 API <br>" +
                    "사진, 사진 게시글, card 는 모두 같은 의미이며 이 문서에서 혼용될 수 있습니다." + "<br>" +
                    "사진(card) API 와 댓글(comment) API 로 구성되어 있습니다." + "<br>" +
                    "<hr>" +
            "<h3>CommentError Code</h3>" +
                    "Comment API 에서 Request Fail 시 발생할 수 있는 Code 입니다." + "<br>" +
                    "<dl>" +
                    "<dt>WRONG_COMMENT_ID</dt>" +
                    "<dd> - 찾으려는 Comment ID가 없습니다. </dd>" +

                    "<dt>WRONG_USER_ID</dt>" +
                    "<dd> - 로그인한 User 가 적합한 상태가 아닙니다.</dd>" +

                    "<dt>WRONG_POST_ID</dt>" +
                    "<dd> - 찾으려는 post ID가 없습니다.</dd>" +
                    "</dl>";
}
