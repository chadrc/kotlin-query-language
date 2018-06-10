# Kotlin Query Language

## Basic CRUD
```kotlin
class Post(
    val id: Int,
    val text: String,
    val published: Date,
    val topic: String,
    val views: Int
)
```

### Create
```kotlin
class InsertInput(
    val text: String
)

val query = Insert(Post::class, InsertInput::class) {
    // At least one required
    values {
        Post::text eq "Some Content"
        Post::topic eq "Food"
    }
    
    // Insert more than one
    values {
        Post::text eq "More Content"
        Post::topic eq "Technology"
    }
    
    // Insert with input value
    values {
        Post::text eq InsertInput::text
    }
}
```

### Read
```kotlin
val query = Select(Post::class, Any::class) {
    // Optional Projection
    // If not provided, entire record is returned
    // All chosen field must be all pluses or all minuses
    fields {
        // Pluses return only those fields
        +Post::text
        
        // Minuses return every other field
        -Post::text
    }
    
    // Optional conditions
    // If not provided all records are returned
    where {
        Post::id eq 1
    }
    
    // Optional sorting
    // Only one plus/minus per field
    sort {
        +Post::id // Ascending
        -Post::text // Descending
    }
    
    // Optional max number to return
    limit(10)
    
    // Optional offset to start at
    offset(100)
}
```

#### Count
```kotlin
val query = Count(Post::class, Any::class) {
    where {
        Post::text eq "Hello"
    }
}
```

### Update
```kotlin
val query = Update(Post::class, Any::class) {

    // Set field value
    Post::text toValue "Update text"

    // Special value, to be interpreted by DB
    Post::publish toValue kqlCurrentDate

    // Unset value
    -Post::topic
    unset(Post::topic)

    // 2 ways to do relative math operations values
    Post::views add 2
    Post::views += 2

    Post::views sub 2
    Post::views -= 2

    Post::ranking mul 2
    Post::ranking *= 2

    Post::ranking div 2
    Post::ranking /= 2

    Post::ranking rem 2
    Post::ranking %= 2
    
    // Update only records that pass conditions
    // Update all records, if not provided
    where {
        Post::id eq 1
    }
}
```

### Delete
```kotlin
val query = Delete(Post::class, Any::class) {
    // Deletes nothing by default
    
    // Conditional delete
    where {
        Post::id eq 1
    }
    
    // Delete all
    all()
}
```

#### Short Forms
```kotlin
val insert = kqlInsert<Post, Any> {}
val select = kqlSelect<Post, Any> {}
val count = kqlCount<Post, Any> {}
val update = kqlUpdate<Post, Any> {}
val delete = kqlDelete<Post, Any> {}
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
        Post::id eq 1
        Post::topic ne "Technology"
        Post::published gt jan1
        Post::published gte jan1
        Post::published lt jan30
        Post::published lte jan30
        Post::published within jan1..jan30
        Post::published notWithin jan1..jan30
        Post::topic within listOf("Food", "Photography", "Music")
        Post::topic notWithin listOf("Food", "Photography", "Music")
        
        // Pattern matching, format will depend on implementation
        Post::text matches "T.*"
    }
}
```
### Logical
Logical AND, all conditions in block must be true.
```kotlin
val select = kqlSelect<Post> {
    where {
        all {
            Post::topic ne "Technology"
            Post::published within jan1..jan30
        }
    }
}
```
Logical OR, only one condition in block must be true.
```kotlin
val select = kqlSelect<Post> {
    where {
        any {
            Post::topic eq "Technology"
            Post::published eq jan30
        }
    }
}
```
Logical NOT, negates conditions on block.

*** Provisional ***
```kotlin
val select = kqlSelect<Post> {
    where {
        not {
            Post::published within jan1..jan30 
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
            Post::published within jan1..jan30
            any {
                Post::topic eq "Technology"
                Post::topic eq "Food"
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
        Post::author // Selects all fields on author
        Post::author withFields {
            Post::firstName
            Post::lastName
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