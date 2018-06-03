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
        +it::text
        
        // Minuses return every other field
        -it::text
    }
    
    // Optional conditions
    // If not provided all records are returned
    where {
        it::id eq 1
    }
    
    // Optional sorting
    // Only one plus/minus per field
    sort {
        +it::id // Ascending
        -it::text // Descending
    }
    
    // Optional max number to return
    limit(10)
    
    // Optional offset to start at
    offset(100)
}
```

#### Count
```kotlin
val query = KQueryCount(Post::class) {
    where {
        it::text eq "Hello"
    }
}
```

### Update
```kotlin
val query = KQueryUpdate(Post::class) {
    // Update all records
    set {
        it::text to "Update text"
    }
    
    // Conditional update
    where {
        it::id eq 1
        
        set {
            it::text to "Conditional update"
        }
    }
}
```

### Delete
```kotlin
val query = KQueryDelete(Post::class) {
    // Deletes nothing by default
    
    // Conditional delete
    where {
        it::id eq 1
    }
    
    // Delete all
    all()
}
```