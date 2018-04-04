/*
 * Copyright (C) 2017 Tobias Hess
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.framersoft.easypasswordgen.view.ratingBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import de.framersoft.easypasswordgen.R;

/**
 * A {@link View} that is used to display a rating from 1 to 3
 * as Bars with Bars and different colors. A Rating of 1 is one
 * filled Bar in red, 2 is two filled Bars in yellow and 3 is
 * 3 filled Bars in green.
 * @author Tobias Hess
 * @since 20.07.2017
 */
public class RatingBarView extends View {

    /*
     * constants for the color of different ratings
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private static final int COLOR_RATING_0 = 0xffbdbdbd;
    private static final int COLOR_RATING_1 = 0xfff44336;
    private static final int COLOR_RATING_2 = 0xffffeb3b;
    private static final int COLOR_RATING_3 = 0xff4caf50;

    /**
     * the rating to display
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private int rating = 0;

    /**
     * stores the bounding rectangles of the bars
     * @author Tobias Hess
     * @since 21.07.2017
     */
    private Rect[] barBoundRects;

    /**
     * constructor
     * @param context
     *      the context the {@link View} is in
     * @param attrs
     *      the attributes for the {@link View}
     */
    public RatingBarView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    /**
     * returns the color for the given rating
     * @param rating
     *      the rating to get the color for
     * @throws UnsupportedOperationException
     *      gets thrown if the rating given is not in the range of 1 <= n <= 3
     * @return
     *      the color for the given rating
     */
    private int getRatingColor(int rating){
        switch (rating){
            case 0:
                return COLOR_RATING_0;
            case 1:
                return COLOR_RATING_1;
            case 2:
                return COLOR_RATING_2;
            case 3:
                return COLOR_RATING_3;
            default:
                throw new UnsupportedOperationException("no color for the given rating");
        }
    }

    /**
     * sets the rating to display
     * @author Tobias Hess
     * @since 20.07.2017
     * @param rating
     *      the rating the {@link RatingBarView} will display
     */
    public void setRating(int rating){
        this.rating = rating;
        invalidate();
    }

    /**
     * @author Tobias Hess
     * @since 20.07.2017
     * @return
     *      the rating the {@link RatingBarView} will display
     */
    public int getRating(){
        return rating;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //padding shall be around 10% of the width / height of the view
        final int padding = (int) Math.ceil(w / 100D * 10);

        //spacing between bars
        final int barSpacing = (int) (Math.ceil(h / 100D)  * 2);

        //calculate the height of a single bar
        final int barHeight = (int)(Math.ceil((h - 4 * barSpacing) / 3D));

        //calculate bounding boxes for the bars
        barBoundRects = new Rect[3];

        //bottom bar
        barBoundRects[0] = new Rect();
        barBoundRects[0].left = padding;
        barBoundRects[0].top = h - barSpacing - barHeight;
        barBoundRects[0].right = w - padding;
        barBoundRects[0].bottom = barBoundRects[0].top + barHeight;

        //middle bar
        barBoundRects[1] = new Rect();
        barBoundRects[1].left = padding;
        barBoundRects[1].top = barBoundRects[0].top - barSpacing - barHeight;
        barBoundRects[1].right = w - padding;
        barBoundRects[1].bottom = barBoundRects[1].top + barHeight;

        //top bar
        barBoundRects[2] = new Rect();
        barBoundRects[2].left = padding;
        barBoundRects[2].top = barBoundRects[1].top - barSpacing - barHeight;
        barBoundRects[2].right = w - padding;
        barBoundRects[2].bottom = barBoundRects[2].top + barHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int barColor = getRatingColor(getRating());
        for(int i = 0; i < barBoundRects.length; i++){
            //get the shape that will be drawn as bar
            GradientDrawable d = (GradientDrawable)ResourcesCompat.getDrawable(getResources(), R.drawable.rectangle, null);
            if(i == 0){
                d = (GradientDrawable)ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_bottom_rectangle, null);
            }
            else if(i >= barBoundRects.length - 1){
                d = (GradientDrawable)ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_top_rectangle, null);
            }

            //determine the fill color
            if (d != null) {
                d.setColor(barColor);
                if (i >= rating) {
                    d.setColor(0x00000000);
                }

                d.setStroke(2, barColor);
                d.setBounds(barBoundRects[i]);
                d.draw(canvas);
            }
        }
    }
}
