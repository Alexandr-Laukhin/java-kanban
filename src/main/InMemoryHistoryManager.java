package main;

import classes.Node;
import classes.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> historyMap = new HashMap<>();
    private List<Node> linkedHistory = new ArrayList<>();

    private Node headOfList;
    private Node tailOfList;


    public void linkLast(Task task) {
        Node node = new Node(null, task, null);
        if (linkedHistory.isEmpty()) {
            linkedHistory.add(node);
            headOfList = node;
            tailOfList = node;
        } else {
            Node oldTail = tailOfList;
            node.setPrevTask(oldTail);  // назначаю старый хвост предыдущим узлом нынешнего хвоста
            oldTail.setNextTask(node);  // в старом хвосте указываю следующий узел как новый хвост
            tailOfList = node;  // новый хвост теперь этот узел
            if (linkedHistory.contains(node)) {  // проверка на повтор
                linkedHistory.remove(node);
            }
            linkedHistory.add(node);
        }
    }  // в этом методе комментарии для себя прописал, чтобы в будущем проще было разобраться. если ты не против, я бы их не удалял.

    public List<Node> getTasks() {
        return linkedHistory;
    }

    private void removeNode(Node node) {
        linkedHistory.remove(node);
    }


    @Override
    public List<Task> getHistory() {
        ArrayList<Task> historyListFromLinkedHistory = new ArrayList<>();
        for (Node node : linkedHistory) {
            historyListFromLinkedHistory.add(node.getThisTask());
        }
        return new ArrayList<>(historyListFromLinkedHistory);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            linkLast(task);
            historyMap.put(task.getId(), tailOfList);
        }
    }

    @Override
    public void remove(int id) {
        linkedHistory.remove(historyMap.get(id));
        historyMap.remove(id);
    }

}




