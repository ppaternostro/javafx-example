package com.pasquasoft.application.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasquasoft.application.exception.NotFoundException;
import com.pasquasoft.application.exception.RestApiException;
import com.pasquasoft.application.model.Post;

public class PostClient
{
  private final HttpClient client = HttpClient.newHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final String BASE_URL = "https://jsonplaceholder.typicode.com/posts/";

  public List<Post> getAllPosts() throws IOException, InterruptedException
  {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 404)
    {
      throw new NotFoundException("Posts not found!");
    }
    else if (response.statusCode() != 200)
    {
      throw new RestApiException(
          String.format("Error retrieving post objects! Status code: %d", response.statusCode()));
    }

    return objectMapper.readValue(response.body(), new TypeReference<>() {
    });
  }

  public Post getPost(Integer id) throws IOException, InterruptedException
  {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + id)).GET().build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 404)
    {
      throw new NotFoundException(String.format("Post with id %d not found!", id));
    }
    else if (response.statusCode() != 200)
    {
      throw new RestApiException(
          String.format("Error retrieving post object with id %d! Status code: %d", id, response.statusCode()));
    }

    return objectMapper.readValue(response.body(), Post.class);
  }

  public void deletePost(Integer id) throws IOException, InterruptedException
  {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + id)).DELETE().build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200)
    {
      throw new RestApiException(
          String.format("Error deleting specified post object! Status code: %d", response.statusCode()));
    }
  }

  public Post createPost(Post post) throws IOException, InterruptedException
  {
    HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json").uri(URI.create(BASE_URL))
        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(post))).build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 201)
    {
      throw new RestApiException(
          String.format("Error creating new post object! Status code: %d", response.statusCode()));
    }

    return objectMapper.readValue(response.body(), Post.class);
  }

  public Post updatePost(Post post) throws IOException, InterruptedException
  {
    HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
        .uri(URI.create(BASE_URL + post.getId()))
        .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(post))).build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200)
    {
      throw new RestApiException(
          String.format("Error updating specified post object! Status code: %d", response.statusCode()));
    }

    return objectMapper.readValue(response.body(), Post.class);
  }
}
