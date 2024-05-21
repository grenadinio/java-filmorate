# java-filmorate

![Схема базы данных приложения Filmorate](https://github.com/grenadinio/java-filmorate/raw/main/src/main/resources/images/Filmorate.png)
Примеры запросов:

1. **Получение списка друзей пользователя:**

```sql
SELECT u.name 
FROM user_friends AS uf
JOIN user AS u ON uf.friendId = u.id
WHERE uf.userId = 1 AND uf.status = 'confirmed';
```

2. **Получение списка фильмов, которые понравились пользователю:**

```sql
SELECT f.name
FROM user_film_likes AS ufl
JOIN film AS f ON f.id = ufl.filmId
WHERE userId = 1;
```

3. **Получение списка фильмов по жанру:**

```sql
SELECT f.name 
FROM film AS f
JOIN film_henre AS fg ON f.id = fg.filmId
WHERE fg.genreId = 1;
```

4. **Получение топ-10 самых популярных фильмов:**

```sql
SELECT 
    f.name, 
    COUNT(ufl.filmId) AS likes_count
FROM 
    film AS f
JOIN 
    user_film_likes AS ufl ON f.id = ufl.filmId
GROUP BY 
    f.id, f.name
ORDER BY 
    likes_count DESC
LIMIT 10;
```