package main;

import classes.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    private static class Node {
        private Node prev = null;
        private Node next = null;
        private Task thisTask;

        public Node(Task thisTask) {
            this.thisTask = thisTask;
        }
    }

    public void linkLast(Task task) {
        Node node = new Node(task);                             //создаем новую ноду, задавая ей сразу ссылку на хвост
        node.prev = tail;
        if (head == null) {                                     //если пока нод нет, делаем новую ноду первой
            head = node;
        } else {
            tail.next = node;                                   //Если не единственная, даем на нее ссылку предыдущей последней ноде
        }
        tail = node;                                            //делаем последней
        historyMap.put(tail.thisTask.getId(), tail);
    }

    private void removeNode(Node node, int id) {
        node = historyMap.remove(id);                                  //мы можем сразу удалить ноду из хешмапы и проверить на null
        if (node == null) {                                            //если null значит ноды нет и нечего удалять - выходим из метода
            return;
        }

        if (node.prev != null) {                                       //если есть предыдущая нода, значит удаляем из центра
            node.prev.next = node.next;                                //делаем соседними предыдущую и следующую ноды удаляемой ноды
        } else {                                                       //node == head
            head = node.next;                                          //следующая за удаляемой нодой становится первой
        }

        if (node.next != null) {                                       //если есть предыдущая нода, значит удаляем из центра
            node.next.prev = node.prev;                                //делаем соседними предыдущую и следующую ноды удаляемой ноды
        } else {                                                       //node == tail
            tail = node.prev;                                          //предыдущая за удаляемой нодой становится последней
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null) {
            history.add(node.thisTask);
            node = node.next;
        }
        return history;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            removeNode(historyMap.get(task.getId()), task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id), id);
    }
}




