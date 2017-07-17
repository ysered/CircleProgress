# CircleProgress
This sample project shows how to develop custom progress view.
You can modify it for more adjustments.

Currently there are two version of circular progress: [SemiCircleProgressView.kt](https://github.com/ysered/CircleProgress/blob/progress-new/app/src/main/java/com/ysered/circleprogress/view/SemiCircleProgressView.kt) and [CircleProgressView.kt] (https://github.com/ysered/CircleProgress/blob/progress-new/app/src/main/java/com/ysered/circleprogress/view/CircleProgressView.kt)

### How it looks like?

**SemiCircleProgressView**
```xml
<com.ysered.circleprogress.view.SemiCircleProgressView
    android:layout_width="200dp"
    android:layout_height="200dp"
    app:roundedProgressStroke="true"
    app:progressStrokeWidth="12dp"
    app:showGradientProgress="true"
    app:startProgressColor="@color/startProgressColor"
    app:endProgressColor="@color/endProgressColor"
    app:progressPathColor="@color/progressPathColor"
    app:progressTextColor="@color/progressTextColor"
    app:startAngle="45"
    app:endAngle="270"
    app:maxProgress="100" />
```

<p align="center">
    <img alt="Progress View" src="http://i.imgur.com/eYjHRv5.png" />
</p>

**CircleProgressView**

```xml
<com.ysered.circleprogress.view.CircleProgressView
        android:layout_width="250dp"
        android:layout_height="250dp"
        progressStrokeWidth="4dp"
        selectedColor="#75dacf"
        actionText="ACTION"
        actionTextSize="20sp"
        progressTextSize="40sp"
        progressTextColor="#aeaeae" />
```

<p align="center">
    <img alt="Progress View" src="http://i.imgur.com/nzFNhZD.png" />
</p>
