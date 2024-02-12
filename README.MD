# Kotlin Ext

JetBrains Kotlin extension makes coding in Kotlin more enjoyable.

# Features

## Intention

1. Flip common
 
   ```kotlin
   foo(a, b)
   ```
   
   to
 
   ```kotlin
   foo(b, a)
   ```

2. move to property

    ```kotlin
   fun foo() {
        val greeting = "Hello world!"
   }
   ```
   to
    ```kotlin
   private val greeting = "Hello world!"
   
   fun foo() {
   }
   ```

## Quick fix

1. merge same if
    
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

2. replace with loop

   ```kotlin
   plus()
   plus()
   plus()
   ```
   
   to
   
   ```kotlin
   for(i in 1..3) {
     plus()
   }
   ``` 
