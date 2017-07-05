# CircleProgress
This sample project shows how to develop custom progress view.
You can modify it for more adjustments.

#### Currently this view supports attributes: 
* progress gradient colors
* progress path color
* progress line width
* progress text

You can take a look on its implementation: [CircleProgressView.java](https://github.com/ysered/CircleProgress/blob/master/app/src/main/java/com/ysered/circleprogress/view/CircleProgressView.java)

For those who interested in learning Kotlin you can find same implementation in this language: [CircleProgressViewKt.kt](https://github.com/ysered/CircleProgress/blob/master/app/src/main/java/com/ysered/circleprogress/view/CircleProgressViewKt.kt)

### How it looks like?

**Layout**
```xml
<com.ysered.circleprogress.view.CircleProgressViewKt
    android:id="@+id/progressBar"
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

**Inside activity**
```kotlin
//...   
val progressBar = findViewById(R.id.progressBar) as CircleProgressViewKt
val progressSeekBar = findViewById(R.id.seekBar) as SeekBar
progressBar.progress = progressSeekBar.progress

progressSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        progressBar.progress = progress
        progressBar.progressText = when (progress) {
            in 0..40 -> "Good"
            in 40..70 -> "Normal"
            else -> "Bad"
        }
    }
    //...
}
//...
```

<p align="center">
    <img alt="Progress View" src="http://i.imgur.com/eYjHRv5.png" />
</p>
