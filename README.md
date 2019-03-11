### 1.在project中的build.gradle

    allprojects {
        repositories {
            ...
            //添加仓库路径
            maven { url "https://jitpack.io" }
        }
    }

### 2.在module中的build.gradle

    dependencies {
        ...
        //添加DYLibrary
        implementation 'com.github.liudingyi:DYLibrary-Kotlin:@version'
    }
