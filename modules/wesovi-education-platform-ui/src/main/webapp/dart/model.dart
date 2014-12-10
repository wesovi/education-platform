library model;

class Task {
  int id;
  String assignee, content;

  Task(this.id, this.assignee, this.content);
  
  Task.fromJson(Map json) {
    id = json['id'];
    assignee = json['assignee'];
    content = json['content'];
  }
}