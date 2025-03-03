package com.pasquasoft.application.model;

import java.util.Objects;

public class Post implements Cloneable
{
  private Integer id;
  private Integer userId;
  private String title;
  private String body;

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public Integer getUserId()
  {
    return userId;
  }

  public void setUserId(Integer userId)
  {
    this.userId = userId;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getBody()
  {
    return body;
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Post other = (Post) obj;
    return Objects.equals(id, other.id);
  }

  @Override
  public String toString()
  {
    return "Post [id=" + id + ", userId=" + userId + ", title=" + title + ", body=" + body + "]";
  }

  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
