#自定义View学习笔记
###1、Configuration
Configuration是用来描述设备的配置信息<br>
比如用户的配置信息：locale和scaling等<br>
比如设备的相关信息：输入模式，屏幕大小，屏幕方向<br>
```
 //获取国家码
        int countryCode = configuration.mcc;

        //获取网络码
        int networkCode = configuration.mnc;

        //判断横竖屏
        if (configuration.orientation == Configuration.UI_MODE_TYPE_NORMAL){

        } else {

        }
```
###2、ViewConfiguration
ViewConfiguration提供了一些自定义控件用到的标准常量，比如UI超时，尺寸大小，滑动距离，敏感度。<br>
ViewConfiguration的实例获取
```
ViewConfiguration viewConfiguration = ViewConfiguration.get(this);

 //获取touchSlop
        int touchSlop = viewConfiguration.getScaledTouchSlop();
        //获取Fling速度的最小值和最大值
        int minimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        int macimumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();

        //判断是否有物理按键
        boolean isHavePermanentMenuKey = viewConfiguration.hasPermanentMenuKey();

//        ViewConfiguration  常量静态方法
        //双击间隔时间,在该时间内是双击,否则是单击
        int doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
        //按住状态转变为长按状态需要的时间
        int longPressTimeout = ViewConfiguration.getLongPressTimeout();
        //重复按键的时间
        int keyRepeatTimeout = ViewConfiguration.getKeyRepeatTimeout();

```
###3、GestureDetector
主要作用是简化Touch操作
```
class GestureListenerImpl implements GestureDetector.OnGestureListener{

        //触摸屏幕的额时候均会调用该方法
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            Log.d("GestureListenerImpl", "手势中的onDown方法");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.d("GestureListenerImpl", "手势中的onShowPress方法");
        }

        //轻击屏幕的时候会调用该方法
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Log.d("GestureListenerImpl", "手势中的onSingleTapUp的方法");
            return false;
        }

        //手指长在屏幕上滚动的时候会调用该方法
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.d("GestureListenerImpl", "手势中的onScroll方法");
            return false;
        }

        //手指在屏幕上长按的时候会调用该方法
        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.d("GestureListenerImpl", "手势中的onlongPress方法");
        }

        //手指在屏幕上拖动的时候会调用该方法
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.d("GestureListenerImpl", "手势中的onFling方法");
            return false;
        }
    }

```
###4、VelocityTracker
VelocityTracker用于跟踪触摸屏时间(比如：Finging及其他Gestures手势时间等)的速率。
```
private void stopVelocityTracker() {
        if (mVelocityTracker != null){
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private int getVelocity() {
        //设置VelocityTracker单位,1000表示1秒内运动的像素
        mVelocityTracker.computeCurrentVelocity(1000);
        //获取1秒内x方向所滑动的像素值
        int xVelocity = (int) mVelocityTracker.getXVelocity();

        return Math.abs(xVelocity);

    }

    private void startVelocityTracker(MotionEvent event){
        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
```
###5、Scroller
#####scrollTo()和scrollBy()的关系
#####Scroll的本质
滑动的时候，只是滑动了View的内容
#####scrollTo()和scrollBy()方法的坐标说明
###6、ViewDragHelper

##
####1、MeasureSpec概述
```
A MeasureSpec encapsulates the layout requirements passed from parent to child. Each MeasureSpec represents a requirement for either the width or the height. A MeasureSpec is comprised of a size and a mode. There are three possible modes:

UNSPECIFIED(未确定的)
The parent has not imposed any constraint on the child. It can be whatever size it wants.
EXACTLY(确切的)
The parent has determined an exact size for the child. The child is going to be given those bounds regardless of how big it wants to be.
AT_MOST(最多)
The child can be as large as it wants up to the specified size.
MeasureSpecs are implemented as ints to reduce object allocation. This class is provided to pack and unpack the <size, mode> tuple into the int.

```
####2、MessureSpec的三种模式
```
   //View.MeasureSpec me = new View.MeasureSpec();
        //获取mode
        int specMode = View.MeasureSpec.getMode(measureSpec);

        //获取size
        int specSize = View.MeasureSpec.getSize(measureSpec);

        //生产MeasureSpec
        int measureSpec = View.MeasureSpec.makeMeasureSpec(specSize,specMode);

```
####3、MeasuresPpec源码解析
```
/**
     * Ask one of the children of this view to measure itself, taking into
     * account both the MeasureSpec requirements for this view and its padding
     * and margins. The child must have MarginLayoutParams The heavy lifting is
     * done in getChildMeasureSpec.
     *
     * @param child The child to measure
     * @param parentWidthMeasureSpec The width requirements for this view
     * @param widthUsed Extra space that has been used up by the parent
     *        horizontally (possibly by other children of the parent)
     * @param parentHeightMeasureSpec The height requirements for this view
     * @param heightUsed Extra space that has been used up by the parent
     *        vertically (possibly by other children of the parent)
     */
protected void measureChildWithMargins(View child,
            int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
//获取到子view的布局
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
		//获取到子view的宽
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        //获取到子view的高
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
//想要获取子view的宽和高，多多少少有父view的宽和高 (要确定子view的MesasureSpec，和父view有关系)
```

```
/**
     * Does the hard part of measureChildren: figuring out the MeasureSpec to
     * pass to a particular child. This method figures out the right MeasureSpec
     * for one dimension (height or width) of one child view.
     * <p>
     * The goal is to combine information from our MeasureSpec with the
     * LayoutParams of the child to get the best possible results. For example,
     * if the this view knows its size (because its MeasureSpec has a mode of
     * EXACTLY), and the child has indicated in its LayoutParams that it wants
     * to be the same size as the parent, the parent should ask the child to
     * layout given an exact size.
     *
     * @param spec           The requirements for this view
     * @param padding        The padding of this view for the current dimension and
     *                       margins, if applicable
     * @param childDimension How big the child wants to be in the current
     *                       dimension
     * @return a MeasureSpec integer for the child
     */
    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec); //获取父容器
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);  //获取竖直方向或者垂直方向,最大空间

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {  //以父容器为基准来进行判断
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) { //也就是自定义的大小
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;  //设置为精确模式
                } else if (childDimension == LayoutParams.MATCH_PARENT) {  //-1
                    // Child wants to be our size. So be it.
                    resultSize = size;  //父view的size
                    resultMode = MeasureSpec.EXACTLY;  //也是精确模式
                } else if (childDimension == LayoutParams.WRAP_CONTENT) { // -2
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;  //  父view的size
                    resultMode = MeasureSpec.AT_MOST;  //最大 为父容器的size
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        //noinspection ResourceType
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }
//可以看出，子view的模式，是根据父容器和子view共同决定的。而不是任意的一个决定的
```
####4、MeasureeSpec总结
![](http://ww2.sinaimg.cn/mw690/006jcGvzgw1fb8td1mve7j314k0g4tbu.jpg)

![](http://ww4.sinaimg.cn/mw690/006jcGvzgw1fb8tfudtenj313c0j6adt.jpg)

![](http://ww1.sinaimg.cn/mw690/006jcGvzgw1fb8thlx7nrj312q0lowjw.jpg)
###5、onMeasure()源码流程
* 1、在onMeasure调用setMeasuredDimension()设置View的宽和高
* 2、在setMeasuredDimension()调用getDefaultSize()获取View的宽和高
* 3、在getDefaultSize()方法中又会调用到getSuggestedMinimumWidth()或getSuggestedMinimumHeight()获取到View的宽和高的最小值



###6、layout源码流程

ViewGroup首先执行了layout方法确定了自己在它父view当中的位置，然后又会去执行onLayout方法从而确定了每个子view的位置，然后每个子view又会去执行layout方法去确定自己在ViewGroup当中的位置。
> layout方法view确定自己本身在父ViewGroup当中的位置，ViewGroup的onLayout方法用于确定子view的位置
```
  @SuppressWarnings({"unchecked"})
    public void layout(int l, int t, int r, int b) {
        if ((mPrivateFlags3 & PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT) != 0) {
            onMeasure(mOldWidthMeasureSpec, mOldHeightMeasureSpec);
            mPrivateFlags3 &= ~PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
        }

        int oldL = mLeft;
        int oldT = mTop;
        int oldB = mBottom;
        int oldR = mRight;

        //确定view在父view的位置
        boolean changed = isLayoutModeOptical(mParent) ?
                setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);

        if (changed || (mPrivateFlags & PFLAG_LAYOUT_REQUIRED) == PFLAG_LAYOUT_REQUIRED) {
            onLayout(changed, l, t, r, b);  //如果满足了变化
            mPrivateFlags &= ~PFLAG_LAYOUT_REQUIRED;

            ListenerInfo li = mListenerInfo;
            if (li != null && li.mOnLayoutChangeListeners != null) {
                ArrayList<OnLayoutChangeListener> listenersCopy =
                        (ArrayList<OnLayoutChangeListener>)li.mOnLayoutChangeListeners.clone();
                int numListeners = listenersCopy.size();
                for (int i = 0; i < numListeners; ++i) {
                    listenersCopy.get(i).onLayoutChange(this, l, t, r, b, oldL, oldT, oldR, oldB);
                }
            }
        }

        mPrivateFlags &= ~PFLAG_FORCE_LAYOUT;
        mPrivateFlags3 |= PFLAG3_IS_LAID_OUT;
    }
```
可以看到在layout方法中先去确定子view的位置是否改变，如果有改变，也就是``` //确定view在父view的位置
        boolean changed = isLayoutModeOptical(mParent) ?
                setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);```这个为true，看源码可以看到setOpticalFrame方法最终也会调用setFrame方法的。在setFrame方法中```
        //会把新传进来的四个值和原来的做对比  只要其中的一个发生了变换，表明view的大小发生了变化
        if (mLeft != left || mRight != right || mTop != top || mBottom != bottom) {
            changed = true; //变换状体改为true```这个判断进入后，会改变子view的各种位置，然后传值
```
最后当子view的位置也确定了之后，会去调用onLayout方法
/**
     * Called from layout when this view should
     * assign a size and position to each of its children.
     *
     * Derived classes with children should override
     * this method and call layout on each of
     * their children.
     * @param changed This is a new size or position for this view
     * @param left Left position, relative to parent
     * @param top Top position, relative to parent
     * @param right Right position, relative to parent
     * @param bottom Bottom position, relative to parent
     */
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }
```
我们发现，这个方法是一个空的方法，什么都没做，但是解释的一时时候，当当前的这个view需要指定大小和位置给他的子view的时候调用，那么也就是所有的子view都要实现这个方法，自己去申请摆放空间和位置，可以看到ViewGroup是viwe的子类，我们看下他的onLayout方法吧。
```
    /**
     * 抽象方法  子类必须去实现
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected abstract void onLayout(boolean changed,
                                     int l, int t, int r, int b);
```
这里就可想而知了，就是任何一个继承自ViewGroup的所有子类，都会去实现这个方法，然后摆放自己，这里我们随便看下一个子view的源码，就拿LinearLayout这个子view来看。
```
  @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mOrientation == VERTICAL) {
            layoutVertical(l, t, r, b);
        } else {
            layoutHorizontal(l, t, r, b);
        }
    }
```
```
    /**
     * Position the children during a layout pass if the orientation of this
     * LinearLayout is set to {@link #HORIZONTAL}.
     *
     * @see #getOrientation()
     * @see #setOrientation(int)
     * @see #onLayout(boolean, int, int, int, int)
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void layoutHorizontal(int left, int top, int right, int bottom) {
        final boolean isLayoutRtl = isLayoutRtl();
        final int paddingTop = mPaddingTop;

        int childTop;
        int childLeft;

        // Where bottom of child should go
        final int height = bottom - top;
        int childBottom = height - mPaddingBottom;

        // Space available for child
        int childSpace = height - paddingTop - mPaddingBottom;

        final int count = getVirtualChildCount();

        final int majorGravity = mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        final int minorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;

        final boolean baselineAligned = mBaselineAligned;

        final int[] maxAscent = mMaxAscent;
        final int[] maxDescent = mMaxDescent;

        final int layoutDirection = getLayoutDirection();
        switch (Gravity.getAbsoluteGravity(majorGravity, layoutDirection)) {
            case Gravity.RIGHT:
                // mTotalLength contains the padding already
                childLeft = mPaddingLeft + right - left - mTotalLength;
                break;

            case Gravity.CENTER_HORIZONTAL:
                // mTotalLength contains the padding already
                childLeft = mPaddingLeft + (right - left - mTotalLength) / 2;
                break;

            case Gravity.LEFT:
            default:
                childLeft = mPaddingLeft;
                break;
        }

        int start = 0;
        int dir = 1;
        //In case of RTL, start drawing from the last child.
        if (isLayoutRtl) {
            start = count - 1;
            dir = -1;
        }

        for (int i = 0; i < count; i++) {
            final int childIndex = start + dir * i;
            final View child = getVirtualChildAt(childIndex);
            if (child == null) {
                childLeft += measureNullChild(childIndex);
            } else if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                int childBaseline = -1;

                final LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) child.getLayoutParams();

                if (baselineAligned && lp.height != LayoutParams.MATCH_PARENT) {
                    childBaseline = child.getBaseline();
                }

                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        childTop = paddingTop + lp.topMargin;
                        if (childBaseline != -1) {
                            childTop += maxAscent[INDEX_TOP] - childBaseline;
                        }
                        break;

                    case Gravity.CENTER_VERTICAL:
                        // Removed support for baseline alignment when layout_gravity or
                        // gravity == center_vertical. See bug #1038483.
                        // Keep the code around if we need to re-enable this feature
                        // if (childBaseline != -1) {
                        //     // Align baselines vertically only if the child is smaller than us
                        //     if (childSpace - childHeight > 0) {
                        //         childTop = paddingTop + (childSpace / 2) - childBaseline;
                        //     } else {
                        //         childTop = paddingTop + (childSpace - childHeight) / 2;
                        //     }
                        // } else {
                        childTop = paddingTop + ((childSpace - childHeight) / 2)
                                + lp.topMargin - lp.bottomMargin;
                        break;

                    case Gravity.BOTTOM:
                        childTop = childBottom - childHeight - lp.bottomMargin;
                        if (childBaseline != -1) {
                            int descent = child.getMeasuredHeight() - childBaseline;
                            childTop -= (maxDescent[INDEX_BOTTOM] - descent);
                        }
                        break;
                    default:
                        childTop = paddingTop;
                        break;
                }

                if (hasDividerBeforeChildAt(childIndex)) {
                    childLeft += mDividerWidth;
                }

                childLeft += lp.leftMargin;
                setChildFrame(child, childLeft + getLocationOffset(child), childTop,
                        childWidth, childHeight);
                childLeft += childWidth + lp.rightMargin +
                        getNextLocationOffset(child);

                i += getChildrenSkipCount(child, childIndex);
            }
        }
    }
```
以上的方法比较多，最后会调用setChildFrame方法。
```
  private void setChildFrame(View child, int left, int top, int width, int height) {
        //这个地方就会去调用view的layout方法进行摆放了
        child.layout(left, top, left + width, top + height);
    }
```
这个也就是最上面给的layout解析。**也就是下面的这个总结**。
ViewGroup首先执行了layout方法确定了自己在它父view当中的位置，然后又会去执行onLayout方法从而确定了每个子view的位置，然后每个子view又会去执行layout方法去确定自己在ViewGroup当中的位置。
> layout方法view确定自己本身在父ViewGroup当中的位置，ViewGroup的onLayout方法用于确定子view的位置
####总结
1、获取View的测量大小measuredWidth额measuredHeight的时机

* 为了防止测量多次造成在onMeasure方法中获取的不准确性，可以在它的下一个阶段去获取它们，也就是onLayout()方法中获取。

2、getMeasuredWidth()和getWidth()的区别

* 一般情况下值是一样的，但是本质是有区别的，在measure方法结束之后就可以获取getMeasuredWidth(),但是getWidth()是在layout结束之后去调用获取的(时机不同)，
* 计算方式不一样第一个是根据setMeasuredDimension()方法获取，第二个是由控件的右坐标减去控件的左坐标决定的

3、view.getLeft()/getRight() ,view.getBottom()/getTop()的坐标问题
![](http://ww1.sinaimg.cn/mw690/006jcGvzgw1fb8up1vkxgj30u60lywff.jpg)
4、合理的自定义ViewGroup
###8、onDraw解析和实践
####1、onDraw源码解析
```
 /**
     * Manually render this view (and all of its children) to the given Canvas.
     * The view must have already done a full layout before this function is
     * called.  When implementing a view, implement
     * {@link #onDraw(android.graphics.Canvas)} instead of overriding this method.
     * If you do need to override this method, call the superclass version.
     *
     * @param canvas The Canvas to which the View is rendered.
     */
    @CallSuper
    public void draw(Canvas canvas) {
        final int privateFlags = mPrivateFlags;
        final boolean dirtyOpaque = (privateFlags & PFLAG_DIRTY_MASK) == PFLAG_DIRTY_OPAQUE &&
                (mAttachInfo == null || !mAttachInfo.mIgnoreDirtyState);
        mPrivateFlags = (privateFlags & ~PFLAG_DIRTY_MASK) | PFLAG_DRAWN;

        /*
         * Draw traversal performs several drawing steps which must be executed
         * in the appropriate order:
         *
         *      1. Draw the background
         *      2. If necessary, save the canvas' layers to prepare for fading
         *      3. Draw view's content
         *      4. Draw children
         *      5. If necessary, draw the fading edges and restore layers
         *      6. Draw decorations (scrollbars for instance)
         */

        // Step 1, draw the background, if needed
        int saveCount;

        if (!dirtyOpaque) {
            drawBackground(canvas);
        }

        // skip step 2 & 5 if possible (common case)
        final int viewFlags = mViewFlags;
        boolean horizontalEdges = (viewFlags & FADING_EDGE_HORIZONTAL) != 0;
        boolean verticalEdges = (viewFlags & FADING_EDGE_VERTICAL) != 0;
        if (!verticalEdges && !horizontalEdges) {
            // Step 3, draw the content
            if (!dirtyOpaque) onDraw(canvas);

            // Step 4, draw the children
            dispatchDraw(canvas);

            // Overlay is part of the content and draws beneath Foreground
            if (mOverlay != null && !mOverlay.isEmpty()) {
                mOverlay.getOverlayView().dispatchDraw(canvas);
            }

            // Step 6, draw decorations (foreground, scrollbars)
            onDrawForeground(canvas);

            // we're done...
            return;
        }
```
第三步是onDraw(canvas)重要的绘制，我们看下这个方法的定义
```
  /**
     * Implement this to do your drawing.
     * 子类实现这个方法然后自己去绘制  这里是一个空方法，让子view去进行实现，然后写逻辑
     * @param canvas the canvas on which the background will be drawn
     */
    protected void onDraw(Canvas canvas) {
    }
```
第四步是绘制孩子,也就是这个方法dispatchDraw(Canvas canvas),这个方法也是需要子view去复写实现的、
```
 /**
     * Called by draw to draw the child views. This may be overridden
     * by derived classes to gain control just before its children are drawn
     * (but after its own view has been drawn).
     * @param canvas the canvas on which to draw the view
     */
    protected void dispatchDraw(Canvas canvas) {

    }
```
由于一般第二步和第五步可以忽略(非特殊情况不用去管)
那么第六步就是绘制一些前悲剧，比如滚动条什么的
```
  // Step 6, draw decorations (foreground, scrollbars)
            onDrawForeground(canvas);


  /**
     * Draw any foreground content for this view.
     *
     * <p>Foreground content may consist of scroll bars, a {@link #setForeground foreground}
     * drawable or other view-specific decorations. The foreground is drawn on top of the
     * primary view content.</p>
     *
     * @param canvas canvas to draw into
     */
    public void onDrawForeground(Canvas canvas) {
        onDrawScrollIndicators(canvas);
        onDrawScrollBars(canvas);

        final Drawable foreground = mForegroundInfo != null ? mForegroundInfo.mDrawable : null;
        if (foreground != null) {
            if (mForegroundInfo.mBoundsChanged) {
                mForegroundInfo.mBoundsChanged = false;
                final Rect selfBounds = mForegroundInfo.mSelfBounds;
                final Rect overlayBounds = mForegroundInfo.mOverlayBounds;

                if (mForegroundInfo.mInsidePadding) {
                    selfBounds.set(0, 0, getWidth(), getHeight());
                } else {
                    selfBounds.set(getPaddingLeft(), getPaddingTop(),
                            getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
                }

                final int ld = getLayoutDirection();
                Gravity.apply(mForegroundInfo.mGravity, foreground.getIntrinsicWidth(),
                        foreground.getIntrinsicHeight(), selfBounds, overlayBounds, ld);
                foreground.setBounds(overlayBounds);
            }

            foreground.draw(canvas);
        }
    }

```

####2、Canvas和Bitmap和Paint的关系
我们先看下Canvas的官网介绍
>The Canvas class holds the "draw" calls. To draw something, you need 4 basic components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint (to describe the colors and styles for the drawing).

大致意思就是，Canvas会调用draw方法，去绘制something，但是需要四种基本的组件

* 第一个就是要有一个Bitmap(保持像素的位图)，
* 第二个Canvas持有一个画布
* 第三个是一个图元(要绘制什么比如矩形，线，文字，图)
* 第四个就是一个画笔

构建一个Canvas
```
 /**
     * Construct a canvas with the specified bitmap to draw into. The bitmap
     * must be mutable.
     *
     * <p>The initial target density of the canvas is the same as the given
     * bitmap's density.
     *
     * @param bitmap Specifies a mutable bitmap for the canvas to draw into.
     */
    public Canvas(@NonNull Bitmap bitmap) {
        if (!bitmap.isMutable()) {
            throw new IllegalStateException("Immutable bitmap passed to Canvas constructor");
        }
        throwIfCannotDraw(bitmap);
        mNativeCanvasWrapper = initRaster(bitmap);
        mFinalizer = NoImagePreloadHolder.sRegistry.registerNativeAllocation(
                this, mNativeCanvasWrapper);
        mBitmap = bitmap;
        mDensity = bitmap.mDensity;
    }
```
####3、Canvas常用操作
1、canvas.translate(移动坐标系)
![](http://ww3.sinaimg.cn/mw690/006jcGvzgw1fb8wtr82lgj30oy0e8mxh.jpg)
```
 @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setTextSize(70);
        paint.setColor(Color.BLUE);
        canvas.drawText("蓝色字体为translte之前所化",20,80,paint);

        canvas.translate(100,300);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为translte之后所化",20,80,paint);
    }
```
![](http://ww4.sinaimg.cn/mw690/006jcGvzgw1fb8xmzmu84j30e609mmxu.jpg)
2、canvas.rotate
旋转了坐标系
```
 @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setTextSize(70);
        paint.setColor(Color.BLUE);
        canvas.drawText("蓝色字体为rotate之前所化",20,80,paint);

        canvas.rotate(50);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为rotate之后所化",20,80,paint);
    }
```
![](http://ww1.sinaimg.cn/mw690/006jcGvzgw1fb8xmdyrphj30ea0e83zg.jpg)
3、canvas.clipRect
裁剪操作  当canvas执行了这个操作，在剪裁后执行的任何绘制，有效的绘制区间就在剪裁这个区域内。
>剪裁是不可逆的，如果我们需要在剪裁完成并且绘制完成之后，想要恢复绘制区域，在原来的绘制区域进行绘制，我们就需要第四个方法了也就是下面的canvas.save和canvas.restore方法

```
 @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLUE);
        canvas.drawText("蓝色字体为Canvas裁剪之前所绘制",20,80,paint);
        Rect rect = new Rect(20,200,900,1000);
        canvas.clipRect(rect);
        canvas.drawColor(Color.GREEN);
       // canvas.rotate(50);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为canvas裁剪之后所绘制",10,300,paint);
    }
```
![](http://ww1.sinaimg.cn/mw690/006jcGvzgw1fb8xlkpt55j30e80ba750.jpg)
4、canvas.save和canvas.restore
```
 @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLUE);
        canvas.drawText("蓝色字体为Canvas裁剪之前所绘制",20,80,paint);
        canvas.save();

        Rect rect = new Rect(20,200,900,1000);
        canvas.clipRect(rect);
        canvas.drawColor(Color.GREEN);
        // canvas.rotate(50);
        paint.setColor(Color.GRAY);
        canvas.drawText("灰色字体为canvas裁剪之后所绘制",10,300,paint);
        canvas.restore();

        paint.setColor(Color.RED);
        canvas.drawText("红色字体为Canvas恢复裁剪之后所绘制",20,170,paint);
    }
```
![](http://ww2.sinaimg.cn/mw690/006jcGvzgw1fb8xk62wzvj30m609g755.jpg)
>canvas.save();  //一旦执行了save  就表明锁定了画布
        //在干其他的绘制，之前保存的绘制是不会被影响的
        // save之后会生成一个新的图层Layer  透明的图层  之后所进行的操作都会发生在Layer上面
        执行restore之后，会把这个新建的图层放到原来的画板上，接着以后的操作就会影响save之前的绘制。

**save和retore对等使用**

####4、PorteDuffXfrmode
先看下可以实现的效果
![](http://ww1.sinaimg.cn/mw690/006jcGvzgw1fb8yiw7uxaj30fm0cyaav.jpg)
```

    /**
     *
     * @param bitmap  原图片
     * @param pixels  角度
     * @return  带圆角的图片
     */
    private Bitmap getRoundCornerImage(Bitmap bitmap, float pixels) {
        int width= bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap roundCornerBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCornerBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);


        Rect rect = new Rect(0,0,width,height);
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        paint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(bitmap,rect,rect,paint);
        return roundCornerBitmap;
    }
```

![](http://ww1.sinaimg.cn/mw690/006jcGvzgw1fb8ykqteuij30hg0ggq4i.jpg)
####5、Bitmap和Matrix
####6、Shader
####7、PathEffect