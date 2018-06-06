# Kotlin Query Language

## Basic CRUD
```kotlin
class Post(val id: Int, val text: String, val published: Date, val topic: String)
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
    // Optional Projection
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

#### Short Forms
```kotlin
val insert = kqlInsert<Post> {}
val select = kqlSelect<Post> {}
val count = kqlCount<Post> {}
val update = kqlUpdate<Post> {}
val delete = kqlDelete<Post> {}
```

## Where Clause Operators
```kotlin
val jan1 = Date("Jan 1 2018")
val jan30 = Date("Jan 30 2018")
```
### Comparison
```kotlin
val select = kqlSelect<Post> {
    where {
        it::id eq 1
        it::topic ne "Technology"
        it::published gt jan1
        it::published gte jan1
        it::published lt jan30
        it::published lte jan30
        it::published within jan1..jan30
        it::published notWithin jan1..jan30
        it::topic within listOf("Food", "Photography", "Music")
        it::topic notWithin listOf("Food", "Photography", "Music")
        
        // Pattern matching, format will depend on implementation
        it::text matches "T.*"
    }
}
```
### Logical
Logical AND, all conditions in block must be true.
```kotlin
val select = kqlSelect<Post> {
    where {
        all {
            it::topic ne "Technology"
            it::published within jan1..jan30
        }
    }
}
```
Logical OR, only one condition in block must be true.
```kotlin
val select = kqlSelect<Post> {
    where {
        any {
            it::topic eq "Technology"
            it::published eq jan30
        }
    }
}
```
Logical NOT, negates conditions on block.
```kotlin
val select = kqlSelect<Post> {
    where {
        not {
            it::published within jan1..jan30 
        }
    }
}
```
Nesting logical blocks
Find all posts published between Jan 1 and Jan 30, and have either Technology or Food as their topic.
```kotlin
val select = kqlSelect<Post> {
    where {
        all {
            it::published within jan1..jan30
            any {
                it::topic eq "Technology"
                it::topic eq "Food"
            }
        }
    }
}
```
## Sub-Object Field Selection
Selecting fields on a sub-object in same document/record
```kotlin
val select = kqlSelect<Post> {
    fields {
        it::author // Selects all fields on author
        it::author with fieldSet {
            it::firstName
            it::lastName
        }
    }
}
```
Selecting fields on a sub-object in different document/record
```kotlin
val select = kqlSelect<Post> {
    fields { post -> 
        post::author with join<Author> {
            where { author ->
                author::id eq post::authorId
            }
        }
    }
}
```