package classes;

import java.util.Objects;

public class Node {

    private Node prevTask;

    private Node nextTask;

    private Task thisTask;


    public Node(Node prevTask, Task thisTask, Node nextTask) {

        this.thisTask = thisTask;
    }

    public Node getNextTask() {
        return nextTask;
    }

    public void setNextTask(Node nextTask) {
        this.nextTask = nextTask;
    }

    public Node getPrevTask() {
        return prevTask;
    }

    public void setPrevTask(Node prevTask) {
        this.prevTask = prevTask;
    }

    public Task getThisTask() {
        return thisTask;
    }

    public void setThisTask(Task thisTask) {
        this.thisTask = thisTask;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Node node = (Node) object;
        return Objects.equals(thisTask, node.thisTask);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(thisTask);
    }
}
