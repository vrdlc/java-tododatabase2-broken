import java.util.List;
import org.sql2o.*;

public class Task {
  private int id;
  private int categoryId;
  private String description;

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public Task(String description, int categoryId) {
    this.description = description;
    this.categoryId = categoryId;
  }

  @Override
  public boolean equals(Object otherTask) {
    if(!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) &&
             this.getId() == newTask.getId();
    }
  }

  public static List<Task> all() {
    String sql = "SELECT id, description FROM tasks";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Tasks(description) VALUES (:description)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", description)
        .executeUpdate()
        .getKey();
    }
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where id=:id";
      Task task = con.createQuery(sql) //and this
      .addParameter("id", id) //and this
      .executeAndFetchFirst(Task.class);
      return task;
    }
  }

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE tasks SET description = :description WHERE id = :id";
    con.createQuery(sql)
      .addParameter("description", description)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM tasks WHERE id = :id;";
        con.createQuery(sql)
          .addParameter("id", id)
          .executeUpdate();
    }
  }
}
