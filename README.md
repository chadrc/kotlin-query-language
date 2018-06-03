# Kotlin Query Language

## Basic CRUD
```kotlin
class Post(val text: String, val id: Int)
```

### Create
```kotlin
val query = KQueryInsert(Post::class) {
    // At least one required
    +Post("Some content")
    
    // Can add more for multi insert
    +Post("Another post")
}
```

### Read
```kotlin
val query = KQuerySelect(Post::class) {
    // Projection
    // If not provided, entire record is returned
    // All chosen field must be all pluses or all minuses
    fields {
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
    // Only one plus/minus per field
    sort {
        +::id // Ascending
        -::text // Descending
    }
    
    // Optional max number to return
    limit(10)
    
    // Optional offset to start at
    offset(100)
}
```