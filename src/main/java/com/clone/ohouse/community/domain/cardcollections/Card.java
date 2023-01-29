package com.clone.ohouse.community.domain.cardcollections;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.community.domain.Post;
import com.clone.ohouse.community.domain.PostType;
import com.clone.ohouse.community.domain.comment.Comment;
import com.clone.ohouse.utility.auditingtime.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Card extends Post {

    @Column
    private Long hit = 0l;
    @Column
    private Boolean isDelete = false;

    @Enumerated(value = EnumType.STRING)
    private HousingType housingType;

    @Enumerated(value = EnumType.STRING)
    private HouseStyle houseStyle;

    @Enumerated(value = EnumType.STRING)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "card")
    private List<CardContent> cardContents = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public Card(PostType postType, HousingType housingType, HouseStyle houseStyle, Color color, User user) {
        super(postType);
        this.housingType = housingType;
        this.houseStyle = houseStyle;
        this.color = color;
        this.user = user;
    }

    public void update(HousingType housingType, HouseStyle houseStyle, Color color){
        if(housingType != null) this.housingType = housingType;
        if(houseStyle != null) this.houseStyle = houseStyle;
        if(color != null) this.color =  color;
    }

    public void addHit(){
        hit++;
    }


}
