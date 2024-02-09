
# Features

1. quick fix: **merge same if**
    
   ```kotlin
    if (condition1) {
        doSomething()
    }
    if (condition2) {
        doSomething()
    }
    ```
   to
    ```kotlin
   if (condition1 || condition2) {
        doSomething()
   }
   ```
   
2. quick fix: **move to field**

    ```kotlin
   fun foo() {
        val greeting = "Hello world!"
   }
   ```
   to
    ```kotlin
   val greeting = "Hello world!"
   
   fun foo() {
   }
   ```
