package com.lanxiao.doapp.myView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Thinkpad on 2015/10/31.
 * 卡片实体类
 */
public class CardModel {

    private String   title;//标题
    private String   description;//介绍
    private int cardImageDrawable;//卡片的图片
    private Drawable cardLikeImageDrawable;//右下角图片
    private Drawable cardDislikeImageDrawable;//右下角图片

    private OnCardDimissedListener mOnCardDimissedListener = null;

    private OnClickListener mOnClickListener = null;

    public interface OnCardDimissedListener {
        void onLike();
        void onDislike();
    }

    public interface OnClickListener {
        void OnClickListener();
    }



    public CardModel(String title, String description, int cardImage) {
        this.title = title;
        this.description = description;
        this.cardImageDrawable = cardImage;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCardImageDrawable() {
        return cardImageDrawable;
    }

    public void setCardImageDrawable(int cardImageDrawable) {
        this.cardImageDrawable = cardImageDrawable;
    }

    public Drawable getCardLikeImageDrawable() {
        return cardLikeImageDrawable;
    }

    public void setCardLikeImageDrawable(Drawable cardLikeImageDrawable) {
        this.cardLikeImageDrawable = cardLikeImageDrawable;
    }

    public Drawable getCardDislikeImageDrawable() {
        return cardDislikeImageDrawable;
    }

    public void setCardDislikeImageDrawable(Drawable cardDislikeImageDrawable) {
        this.cardDislikeImageDrawable = cardDislikeImageDrawable;
    }

    public void setOnCardDimissedListener( OnCardDimissedListener listener ) {
        this.mOnCardDimissedListener = listener;
    }

    public OnCardDimissedListener getOnCardDimissedListener() {
        return this.mOnCardDimissedListener;
    }


    public void setOnClickListener( OnClickListener listener ) {
        this.mOnClickListener = listener;
    }

    public OnClickListener getOnClickListener() {
        return this.mOnClickListener;
    }
}
