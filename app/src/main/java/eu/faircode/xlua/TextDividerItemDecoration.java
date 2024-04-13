package eu.faircode.xlua;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.faircode.xlua.api.xstandard.interfaces.IDividerKind;

public class TextDividerItemDecoration extends RecyclerView.ItemDecoration {
    //Shitty expiremnt class for Dividers on RecyclerViews
    public enum TextVerticalAlignment {
        TOP, CENTER, BOTTOM
    }

    private final Paint textPaint;
    private final Paint linePaint;
    private String text;
    private int textSize;
    private int textAlignment;
    private final int textPadding;
    private boolean showLineDivider;
    private int lineThickness;
    private boolean dividerAfterItem; // true for after, false for before

    private TextVerticalAlignment textVerticalAlignment = TextVerticalAlignment.CENTER;
    private int textToLinePadding = 0; // Custom padding between text and line

    private int leftBarToStartParentPadding = 0; // Padding from the left side of the parent to the start of the bar
    private int rightBarToEndParentPadding = 0; // Padding from the right side of the parent to the end of the bar

    public TextDividerItemDecoration(Context context) {
        this(context, "");
    }

    public TextDividerItemDecoration(Context context, String text) {
        this.text = text;

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        setTextSize(40); // Default text size

        linePaint = new Paint();
        linePaint.setColor(Color.GRAY); // Set color for bar
        setLineThickness(2); // Default line thickness
        showLineDivider = false; // Divider is disabled by default

        this.textAlignment = Gravity.CENTER; // Default text alignment
        this.textPadding = 10; // Default text padding
        this.dividerAfterItem = true; // By default, place divider after the item
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextVerticalAlignment(TextVerticalAlignment alignment) {
        this.textVerticalAlignment = alignment;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
    }

    public void setTextAlignment(int textAlignment) {
        this.textAlignment = textAlignment;
    }

    public void enableLineDivider(boolean show, int thickness) {
        this.showLineDivider = show;
        setLineThickness(thickness);
    }

    private void setLineThickness(int thickness) {
        this.lineThickness = thickness;
        linePaint.setStrokeWidth(thickness);
    }

    public void setDividerPosition(boolean afterItem) {
        this.dividerAfterItem = afterItem;
    }

    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTopText = 0;
    private int paddingBottomText = 0;

    public void setTextPadding(int left, int right, int top, int bottom) {
        this.paddingLeft = left;
        this.paddingRight = right;
        this.paddingTopText = top;
        this.paddingBottomText = bottom;
    }

    public void setTextPaddingLeft(int textPaddingLeft) {
        this.paddingLeft = textPaddingLeft;
    }

    public void setTextPaddingRight(int textPaddingRight) {
        this.paddingRight = textPaddingRight;
    }

    public void setTextPaddingTop(int textPaddingTop) {
        this.paddingTopText = textPaddingTop;
    }

    public void setTextPaddingBottom(int textPaddingBottom) {
        this.paddingBottomText = textPaddingBottom;
    }

    public void setTextToLinePadding(int paddingSize) {
        this.textToLinePadding = paddingSize;
    }

    public void setLeftBarToStartParentPadding(int paddingSize) {
        this.leftBarToStartParentPadding = paddingSize;
    }

    public void setRightBarToEndParentPadding(int paddingSize) {
        this.rightBarToEndParentPadding = paddingSize;
    }

    // ... (previous code remains the same)

    private float barCornerRadius = 0f; // Default bar corner radius

    // ... (constructor and other methods remain the same)

    public void setBarCornerRadius(float radius) {
        this.barCornerRadius = radius;
    }
    private boolean useIndependentDividers = true; // Default to using independent dividers

    // ... (constructor and other methods remain the same)

    public void setUseIndependentDividers(boolean useIndependentDividers) {
        this.useIndependentDividers = useIndependentDividers;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (useIndependentDividers) {
            drawIndependentDividers(c, parent, state);
        }
    }

    private void drawIndependentDividers(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position == RecyclerView.NO_POSITION) {
                continue;
            }

            if (showLineDivider && (dividerAfterItem || (!dividerAfterItem && position < parent.getAdapter().getItemCount() - 1))) {

                float totalSpace = textSize + 2 * textPadding + lineThickness;
                float lineY = dividerAfterItem ? child.getBottom() + totalSpace / 2 : child.getTop() - totalSpace / 2;

                RectF barRect = new RectF(leftBarToStartParentPadding, lineY - lineThickness / 2,
                        parent.getWidth() - rightBarToEndParentPadding, lineY + lineThickness / 2);
                c.drawRoundRect(barRect, barCornerRadius, barCornerRadius, linePaint);

                if (text != null && !text.isEmpty()) {
                    float textY;
                    switch (textVerticalAlignment) {
                        case TOP:
                            textY = lineY - totalSpace + lineThickness + paddingTopText + textPaint.getFontMetrics().top + textToLinePadding;
                            break;
                        case CENTER:
                            float centerOffset = (textPaint.getFontMetrics().ascent + textPaint.getFontMetrics().descent) / 2;
                            textY = lineY - centerOffset - paddingBottomText + paddingTopText;
                            break;
                        case BOTTOM:
                        default:
                            textY = lineY + (lineThickness / 2) + paddingTopText - paddingBottomText - textPaint.getFontMetrics().bottom - textToLinePadding;
                            break;
                    }
                    float x = calculateTextPositionX(parent.getWidth(), textAlignment, textPaint.measureText(text)) + leftBarToStartParentPadding - paddingRight + paddingLeft;
                    c.drawText(text, x, textY, textPaint);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!useIndependentDividers) {
            drawLinkedDividers(c, parent, state);
        }
    }

    private static final String TAG = "XLua.TextDividerItemDecoration";


    private HashMap<String, String> idHolders = new HashMap<>();


    private List<String> ids = new ArrayList<>();
    private boolean useDividerIdSystem = false;
    public void setLinkDividersToGroupIDs(boolean linkToIds) {
        useDividerIdSystem = linkToIds;
    }

    private boolean isDividerNeeded(int position, RecyclerView parent) {
        try {
            if (!(parent.getAdapter() instanceof IDividerKind))
                return false;

            IDividerKind divKind = (IDividerKind) parent.getAdapter();
            if(divKind.isSearching()) {
                if(!idHolders.isEmpty())
                    idHolders.clear();

                return false;
            }else {
                if(divKind.hasChanged()) {
                    idHolders.clear();
                    divKind.resetHashChanged();
                }
            }

            Log.i(TAG, "Position=" + position);
            String id = divKind.getDividerID(position);
            String longId = divKind.getLongID(position);
            if(position > 0) {
                String lastId = divKind.getDividerID(position - 1);
                String lastIdEx = divKind.getLongID(position - 1);
                Log.i(TAG, "id=" + id + " long id=" + longId + " lastId=" + lastId + "long last id=" + lastIdEx + " pos=" + position);
                if(id.equalsIgnoreCase(lastId))
                    return false;
            }

            if(idHolders.containsKey(id)) {
                String longIdMap = idHolders.get(id);
                return longIdMap != null && longIdMap.equalsIgnoreCase(longId);
            }else {
                idHolders.put(id, longId);
                return true;
            }
        }catch (Exception e) {
            Log.e(TAG, "Failed getting divider state=" + e);
            return false;
        }
    }

    private int dividerTopPadding = 0; // Padding above the divider line

    public void setDividerTopPadding(int padding) {
        this.dividerTopPadding = padding;
    }

    private int dividerBottomPadding = 0;
    private static final int DEFAULT_DIVIDER_BOTTOM_PADDING = 100;
    public void setDividerBottomPadding(int padding) {
        this.dividerBottomPadding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);

        if (useDividerIdSystem && !isDividerNeeded(position, parent)) {
            return; // No extra offset needed.
        }

        // Calculate total offset needed including the top padding of the divider.
        //if ((dividerAfterItem && position > 0) || !dividerAfterItem || !text.isEmpty()) {
        //    int totalSpace = textSize + 2 * textPadding + (showLineDivider ? lineThickness : 0) + dividerTopPadding;
        //    outRect.top = totalSpace;
        //}
        //if ((dividerAfterItem && position > 0) || !dividerAfterItem) {
        //    int totalSpace = textSize + 2 * textPadding + lineThickness + dividerTopPadding;
        //    outRect.top += totalSpace; // Use += to accumulate space above the item.
        //}

        // Only add the offset (padding) below the divider, not above it.

        /*if (dividerAfterItem || (!dividerAfterItem && position > 0)) {
            int totalSpace = textSize + 2 * textPadding + lineThickness;
            if (dividerAfterItem) {
                // Add padding below the divider for the item after it.
                outRect.bottom = dividerTopPadding;
            } else {
                // For dividers before the item, adjust the top offset for subsequent items.
                outRect.top = totalSpace + dividerTopPadding;
            }
        }*/

        // Apply the offset (padding) above the divider.
        if ((dividerAfterItem && position > 0) || !dividerAfterItem) {
            //int totalSpace = textSize + 2 * textPadding + lineThickness;
            //int totalSpace = textSize + lineThickness;

            //int totalSpace = (lineThickness + dividerTopPadding + dividerBottomPadding);
            //This will be the TOTAL space needed (empty) before the Rectangle

            outRect.top = (int)getTotalFreeSpace();  // Apply padding to the top of the divider.
            //outRect.bottom = (int)getTotalFreeSpace();

            //outRect.bottom = DEFAULT_DIVIDER_BOTTOM_PADDING;//totalSpace - dividerBottomPadding - DEFAULT_DIVIDER_BOTTOM_PADDING;
        }
    }

    public void initColors(Context context) {
        //linePaint.setColor(XUtil.resolveColor(context, R.attr.cardBackgroundColor));
        linePaint.setColor(XUtil.resolveColor(context, R.attr.colorDividerBack));
        textPaint.setColor(XUtil.resolveColor(context, R.attr.colorTextDivider));
        //textPaint.setColor(XUtil.resolveColor(context, R.color._primaryTextColor));
        //textPaint.setColor(XUtil.resolveColor(context, Color.WHITE));
        //textPaint.setColor(Color.WHITE);
        //R.attr.colorPrimary;
        //textPaint.setColor(XUtil.resolveColor(context, R.attr.itemTextColor));
        //textPaint.setColor(R.attr.editTextColor);
    }

    public float getTotalFreeSpace() {
        //add alot more logic to this
        return lineThickness + dividerTopPadding + dividerBottomPadding;
    }

    private void drawLinkedDividers(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int width = parent.getWidth();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int pos = parent.getChildAdapterPosition(child);
            if(pos < 0)
                continue;

            if (useDividerIdSystem && !isDividerNeeded(pos, parent))
                continue;

            if(useDividerIdSystem) {
                IDividerKind divKind = (IDividerKind) parent.getAdapter();
                String id = divKind.getDividerID(pos);
                this.text = id;
            }

            float lineY, textY = 0;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

            if (showLineDivider) {
                // Calculate the vertical position of the line based on whether the divider is before or after.
                //float totalSpace = textSize + 2 * textPadding + lineThickness;
                //lineY = dividerAfterItem ?
                //        child.getBottom() + totalSpace / 2 + dividerTopPadding :  // Push down for divider after.
                //        child.getTop() - totalSpace / 2 - dividerTopPadding;  // Pull up for divider before without affecting item above.

                //float totalSpace = textSize + 2 * textPadding + lineThickness;
                //lineY = (dividerAfterItem ? child.getBottom() : child.getTop() - totalSpace) + dividerTopPadding;

                // Adjust the RectF for the bar to account for the padding.
                /*RectF barRect = new RectF(
                        leftBarToStartParentPadding,
                        lineY - lineThickness / 2,
                        width - rightBarToEndParentPadding,
                        lineY + lineThickness / 2
                );*/
                //c.drawRoundRect(barRect, barCornerRadius, barCornerRadius, linePaint);

                //old
                //float totalSpace = textSize + 2 * textPadding + lineThickness + dividerTopPadding - dividerBottomPadding;
                float totalSpace = getTotalFreeSpace();
                float totalSpaceHalf = getTotalFreeSpace() / 2;
                float half = lineThickness / 2.0f;
                float lineYCalculation = 0;
                int itemOffset = 0;

                if(dividerAfterItem) {
                    //We insert DIVIDERs AFTER elements
                    int bottomOffset = child.getBottom();
                    itemOffset = bottomOffset;

                    lineYCalculation = bottomOffset + totalSpaceHalf;     //
                    //someNumber = someNumber / 2;                    //Divide MOST Down Offset by 2 (half) (center)
                    lineYCalculation = lineYCalculation + dividerTopPadding;           //Add Top Padding from CENTER (push center line down)
                    lineYCalculation = lineYCalculation - dividerBottomPadding;        //Sub Bottom Padding from (new center) line (push center line up)
                }else {
                    //We insert DIVIDERs BEFORE elements
                    int topOffset = child.getTop();
                    itemOffset = topOffset;
                    lineYCalculation = topOffset - totalSpaceHalf;        //
                    //someNumber = someNumber / 2;                    //Divide MOST UP by 2 (half) (center)
                    lineYCalculation = lineYCalculation + dividerTopPadding;           //Add Top Padding from CENTER (push center line down)
                    lineYCalculation = lineYCalculation - dividerBottomPadding;        //Sub Bottom Padding from (new center) line (push center line up)
                }

                //The (center) will now be positioned in the correct place given padding : someNumber
                //lineY = someNumber;
                //lineY = someNumber - (totalSpace / 2) + (lineThickness / 2) + dividerTopPadding - dividerBottomPadding;
                lineY = lineYCalculation;
                //lineY = lineY - (totalSpace / 2);
                //lineY = lineY + 500;
                //lineY = lineY + 400;


                float topStart = lineY - half;                  //Sub HALF of Thickness of Line from Custom Center (pushing up)
                float botStart = lineY + half;                  //Add HALF of Thickness of Line from Custom Center (pushing down)

                Log.i(TAG, "total space=" + totalSpace + " half=" + half + " line y calculation=" + lineYCalculation + " topStart=" + topStart + " botStart=" + botStart + " item offset=" + itemOffset + " lineY=" + lineY);

                RectF barRect = new RectF(
                        leftBarToStartParentPadding,                //left
                        topStart,                                   //TOP
                        width - rightBarToEndParentPadding,         //Right
                        botStart);                                  //Bottom

                //float textLineCenter = dividerAfterItem ? child.getBottom() + totalSpace / 2 :
                //        child.getTop() - totalSpace / 2 + dividerTopPadding - dividerBottomPadding;

                //lineY = textLineCenter - (totalSpace / 2) + (lineThickness / 2) + dividerTopPadding - dividerBottomPadding;

                //this applies padding AFTER ?
                //float totalSpace = textSize + 2 * textPadding + lineThickness;
                //float textLineCenter = dividerAfterItem ? child.getBottom() + totalSpace / 2 : child.getTop() - totalSpace / 2;
                //lineY = textLineCenter - (totalSpace / 2) + (lineThickness / 2);

                /*RectF barRect = new RectF(
                        leftBarToStartParentPadding,                //left
                        lineY - lineThickness / 2,                  //TOP
                        width - rightBarToEndParentPadding,         //Right
                        lineY + lineThickness / 2);                 //Bottom

                 */

                c.drawRoundRect(barRect, barCornerRadius, barCornerRadius, linePaint);

                //If we subtract it goes "higher"   (lower number goes higher up)
                //If we add it goes "lower" (bigger number goes lower)
                //line Y is the center line of the line / divider bar (Y axis)

                if (text != null && !text.isEmpty()) {
                    switch (textVerticalAlignment) {
                        case TOP:
                            //textY = lineY - totalSpace + lineThickness + paddingTopText + fontMetrics.top + textToLinePadding;
                            //textY = lineY - totalSpace + lineThickness + paddingTop + fontMetrics.top + textToLinePadding;
                            //textY = (lineY - half) + textSize;      //This will Push it to the top of the inner bar (-half will ensure its inside)
                            //textY = textY - 25;                     //Remove Extra Spacing
                            //textY = textY - paddingBottomText;
                            //textY = textY + paddingTopText;
                            //textY = textY + textToLinePadding;                  //Add final Padding Option
                            textY = (lineY - half);
                            //textY = textY + (textSize / (float)2);           //We add Text size since this will be 'above'
                            //textY = textY + textSize;
                            //textY = textY - 55;
                            //textY = textY + fontMetrics.top;

                            //fontMetrics.ascent

                            textY = textY + Math.abs(fontMetrics.ascent);
                            textY = textY - Math.abs(fontMetrics.descent);

                            //textY = textY - 10;
                            //Log.w(TAG, " top=" + fontMetrics.top + " bottom=" + fontMetrics.bottom);
                            textY = textY - paddingBottomText;
                            textY = Math.abs(textY + paddingTopText);
                            textY = Math.abs(textY + textToLinePadding);
                            break;
                        case CENTER:
                            float centerOffset = (fontMetrics.ascent + fontMetrics.descent) / 2;
                            textY = lineY - centerOffset - paddingBottomText + paddingTopText;
                            break;
                        case BOTTOM:
                            textY = (lineY + half);
                            textY = textY - paddingBottomText;
                            textY = textY + paddingTopText;
                            textY = textY - textToLinePadding;

                            //textY = textY + textSize;
                            //textY = textY - paddingBottomText;
                            //textY = textY + paddingTopText;
                            //textY = textY - textToLinePadding;
                            //textY = lineY + (lineThickness / 2) + paddingTopText - paddingBottomText - fontMetrics.bottom - textToLinePadding;
                            break;
                    }
                    float x = calculateTextPositionX(width, textAlignment, textPaint.measureText(text)) + leftBarToStartParentPadding - paddingRight + paddingLeft;
                    c.drawText(text, x, textY, textPaint);
                }
            } else if (text != null && !text.isEmpty()) {
                float defaultTextY = child.getTop() - textPadding - fontMetrics.bottom - dividerTopPadding;
                float x = calculateTextPositionX(width, textAlignment, textPaint.measureText(text)) + leftBarToStartParentPadding - paddingRight + paddingLeft;
                c.drawText(text, x, defaultTextY, textPaint);
            }
        }
    }

    /*@Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!useIndependentDividers) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);

            // Reset top offset for all items initially
            outRect.top = 0;
            if(useDividerIdSystem)
                if(!isDividerNeeded(position, parent))
                    return;


            if (showLineDivider) {
                // If the divider is 'after', only add top space for items that are not the first
                // If the divider is 'before', add top space for all items, including the first
                if (dividerAfterItem && position > 0) {
                    outRect.top = textSize + 2 * textPadding + lineThickness;
                } else if (!dividerAfterItem) {
                    outRect.top = textSize + 2 * textPadding + lineThickness;
                }
            }
        }
    }*/

    /*@Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);

        // Default top offset for all items
        outRect.top = 0;

        Log.i(TAG, "INVOKING GETITEM OFFSETS POS=" + position);
        if (useDividerIdSystem && !isDividerNeeded(position, parent)) {
            Log.i(TAG, "eesh");
            // Skip setting additional offset for items that do not need a divider.
            // This block intentionally left blank.
            return;
        }

        if (showLineDivider) {
            // If the divider is 'after', only add top space for items that are not the first
            // If the divider is 'before', add top space for all items, including the first
            if (dividerAfterItem && position > 0) {
                outRect.top = textSize + 2 * textPadding + lineThickness;
            } else if (!dividerAfterItem) {
                outRect.top = textSize + 2 * textPadding + lineThickness;
            }
        }
    }*/



    private float calculateTextPositionX(int width, int alignment, float textWidth) {
        switch (alignment) {
            case Gravity.LEFT:
                return paddingLeft;
            case Gravity.RIGHT:
                return width - paddingRight - textWidth - rightBarToEndParentPadding;
            case Gravity.CENTER:
            default:
                return (width - leftBarToStartParentPadding - rightBarToEndParentPadding) / 2f - textWidth / 2f - paddingRight + paddingLeft;
        }
    }

    private float calculateTextYWithoutLine(View child, Paint.FontMetrics fontMetrics) {
        float totalSpace = textSize + 2 * textPadding;
        if (!dividerAfterItem) {
            return child.getTop() - (totalSpace / 2) - (fontMetrics.ascent + fontMetrics.descent) / 2 - paddingBottomText + paddingTopText;
        } else {
            return child.getBottom() + (totalSpace / 2) - (fontMetrics.ascent + fontMetrics.descent) / 2 - paddingBottomText + paddingTopText;
        }
    }

    /*@Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);

        // Reset top offset for all items initially
        outRect.top = 0;

        if (showLineDivider) {
            // If the divider is 'after', only add top space for items that are not the first
            // If the divider is 'before', add top space for all items, including the first
            if (dividerAfterItem && position > 0) {
                outRect.top = textSize + 2 * textPadding + lineThickness;
            } else if (!dividerAfterItem) {
                outRect.top = textSize + 2 * textPadding + lineThickness;
            }
        }
    }*/
}