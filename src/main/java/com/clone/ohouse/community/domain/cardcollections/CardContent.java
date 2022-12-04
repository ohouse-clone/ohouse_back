package com.clone.ohouse.community.domain.cardcollections;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class CardContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_media_file_id")
    private CardMediaFile cardMediaFile;

    @Column(length = 256)
    private String content;
    @Column
    private Integer sequence = 0;

    public CardContent(Card card, CardMediaFile cardMediaFile, String content, Integer sequence) {
        this.card = card;
        this.cardMediaFile = cardMediaFile;
        this.content = content;
        this.sequence = sequence;
    }
    public void update(CardMediaFile cardMediaFile, String content, Integer index){
        if(cardMediaFile != null) this.cardMediaFile = cardMediaFile;
        if(content != null) this.content = content;
        if(sequence != null) this.sequence = sequence;
    }
}
