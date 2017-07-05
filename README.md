# CircleProgress
This sample project shows how to develop custom progress view.
You can modify it for more adjustments.

#### Currently this view supports attributes: 
* progress gradient colors
* progress path color
* progress line width
* progress text

You can take a look on its implementation: [CircleProgressView.java](https://github.com/ysered/CircleProgress/blob/master/app/src/main/java/com/ysered/circleprogress/view/CircleProgressView.java)

For those who interested to lean Kotlin you can find same implementation in this language: [CircleProgressViewKt.kt](https://github.com/ysered/CircleProgress/blob/master/app/src/main/java/com/ysered/circleprogress/view/CircleProgressViewKt.kt)

### How it looks like?

**Styles**

```xml
<style name="CircleProgressStyle">
    <item name="showGradientProgress">true</item>
    <item name="progressBackgroundColor">@color/progressBackground</item>
    <item name="progressPathColor">@color/progressPathColor</item>
    <item name="startProgressColor">@color/startProgressColor</item>
    <item name="endProgressColor">@color/endProgressColor</item>
    <item name="progressStrokeWidth">12dp</item>
    <item name="roundedProgressStroke">true</item>
    <item name="progressTextColor">@color/progressTextColor</item>
    <item name="startAngle">45</item>
    <item name="endAngle">270</item>
    <item name="maxProgress">100</item>
</style>
```

**Layout**
```xml
<com.ysered.circleprogress.view.CircleProgressViewKt
    android:id="@+id/progressBar"
    style="@style/CircleProgressStyle"
    android:layout_width="200dp"
    android:layout_height="200dp" />
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
        if (progress < 40) {
            progressBar.progressText = "Good"
        } else if (progress in 40..70) {
            progressBar.progressText = "Normal"
        } else {
            progressBar.progressText = "Bad"
        }
    }
//...
```

![Progress View](http://i.imgur.com/eYjHRv5.png "Progress View")
