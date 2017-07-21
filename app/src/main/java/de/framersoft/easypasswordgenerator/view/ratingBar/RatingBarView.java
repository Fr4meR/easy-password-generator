/**
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
package de.framersoft.easypasswordgenerator.view.ratingBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

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
     * the paint to use for filled bars
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private Paint paintFilledBar;

    /**
     * the paint to use for unfilled bars
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private Paint paintUnfilledBar;

    private int[][] barData;

    /**
     * constructor
     * @param context
     *      the context the {@link View} is in
     * @param attrs
     *      the attributes for the {@link View}
     */
    public RatingBarView(Context context, AttributeSet attrs){
        super(context, attrs);
        initPainting();
    }

    /**
     * creates the needed paints for drawing
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private void initPainting(){
        paintFilledBar = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFilledBar.setStyle(Paint.Style.FILL);
        paintFilledBar.setColor(getRatingColor(getRating()));

        paintUnfilledBar = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintUnfilledBar.setStyle(Paint.Style.STROKE);
        paintUnfilledBar.setStrokeWidth(3);
        paintUnfilledBar.setStrokeCap(Paint.Cap.SQUARE);
        paintUnfilledBar.setColor(getRatingColor(getRating()));
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
        initPainting();
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
        final int barSpacing = (int) (Math.ceil(h / 100D)  * 5);

        //calculate the height of a single bar
        final int barHeight = (int)(Math.ceil((h - 4 * barSpacing) / 3D));

        //calculate the bar positions
        barData = new int[3][4];

        barData[0][0] =  padding;
        barData[0][1] = h - barSpacing - barHeight;
        barData[0][2] = w - padding;
        barData[0][3] = barData[0][1] + barHeight;

        barData[1][0] = padding;
        barData[1][1] = barData[0][1] - barHeight - barSpacing;
        barData[1][2] = w - padding;
        barData[1][3] = barData[1][1] + barHeight;

        barData[2][0] = padding;
        barData[2][1] = barData[1][1] - barHeight - barSpacing;
        barData[2][2] = w - padding;
        barData[2][3] = barData[2][1] + barHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i = 0; i < barData.length; i++){
            if(i < rating){
                canvas.drawRect(barData[i][0], barData[i][1], barData[i][2], barData[i][3], paintFilledBar);
            }
            else{
                canvas.drawRect(barData[i][0], barData[i][1], barData[i][2], barData[i][3], paintUnfilledBar);
            }
        }
    }
}
