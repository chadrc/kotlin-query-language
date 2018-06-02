# Kotlin Query Language

## Basic CRUD
```kotlin
class Post(val id: Int, val text: String)
```

### Read
```kotlin
val query = KQuery(Post::class) {
    // Projection
    // If not provided, entire record is returned
    // All chosen field must be all pluses or all minuses
    select {
        // Pluses return only those fields
        +::text
        
        // Minuses return every other field
        -::text
    }
    
    // Optional conditions
    where {
        ::id eq 1
    }
    
    // Optional sorting
    order {
        +::id // Ascending
        -::id // Descending
    }
    
    // Optional max number to return
    limit(10)
    
    // Optional offset to start at
    offset(100)
}
```