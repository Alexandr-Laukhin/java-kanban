package main;

import classes.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> historyMap = new HashMap<>();
    private List<Node> linkedHistory = new ArrayList<>();

    private Node headOfList;
    private Node tailOfList;

    private static class Node {
        private Node prevTask;
        private Node nextTask;
        private Task thisTask;

        public Node(Node tailOfList, Task thisTask, Node nextTask) {

            this.thisTask = thisTask;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Node node = (Node) object;
            return Objects.equals(thisTask.getId(), node.thisTask.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(thisTask.getId());
        }
    }

    public void linkLast(Task task) {
        Node node = new Node(tailOfList, task, null);

        if (linkedHistory.isEmpty()) {  // проверка на пустой список
            node.prevTask = null;
            headOfList = node;
            tailOfList = node;
        } else {
            if (historyMap.containsKey(node.thisTask.getId())) {  // проверка на копии и их удаление из хэшмапы и связанного списка
                removeNode(historyMap.get(node.thisTask.getId()));
            }
            tailOfList.nextTask = node;   // сначала говорим, что следующая нода от хвоста - это нода
            int prevId = tailOfList.thisTask.getId();
            tailOfList = node;            // потом нода сама становится хвостом
            tailOfList.prevTask = historyMap.get(prevId); // ставим связь с прошлой нодой
        }

        linkedHistory.add(tailOfList);      // потом добавляем ее в связанный список
        historyMap.put(tailOfList.thisTask.getId(), tailOfList);  // потом добавляем ее в хешмапу
    }

    public List<Node> getTasks() {
        return linkedHistory;
    }

    private void removeNode(Node node) {
        if (linkedHistory.size() == 1) {
            if (linkedHistory.contains(node)) {
                linkedHistory.clear();
                historyMap.clear();
                headOfList = null;
                tailOfList = null;
            }
        } else if (node.thisTask.getId() == historyMap.get(headOfList.thisTask.getId()).thisTask.getId()) {            // если  id ноды совпадает с id head, и (пока удалил проверку предыдущего значения на ноль)
            headOfList = historyMap.get(headOfList.nextTask.thisTask.getId());                                           // если мы удаляем head, то добываем id следующей ноды из хешмапы, и делаем эту ноду head
            headOfList.prevTask = null;                                                                                  // далее обнуляем предыдущее значение у новой head
            // historyMap.get(headOfList.nextTask.thisTask.getId()).prevTask = headOfList;                                  // далее добываем следующую ноду у head, и ставим head у нее как предыдущее значение
            linkedHistory.remove(node);                                                                                      // удаляем ноду из связанного списка
            historyMap.remove(node.thisTask.getId());

        } else if (node.thisTask.getId() == historyMap.get(tailOfList.thisTask.getId()).thisTask.getId()) {
            tailOfList = historyMap.get(tailOfList.prevTask.thisTask.getId());
            tailOfList.nextTask = null;
            historyMap.get(tailOfList.prevTask.thisTask.getId()).nextTask = tailOfList;
            linkedHistory.remove(node);
            historyMap.remove(node.thisTask.getId());

        } else {
            historyMap.get(node.nextTask.thisTask.getId()).prevTask = historyMap.get(node.prevTask.thisTask.getId());    // добываем у ноды id следующей, и у следующей назначаем предыдущее значение как у нынешней ноды.
            historyMap.get(node.prevTask.thisTask.getId()).nextTask = historyMap.get(node.nextTask.thisTask.getId());    // тоже самое, только наоборот
            linkedHistory.remove(node);
            historyMap.get(node.thisTask.getId());

        }
    }

    @Override
    public List<Task> getHistory() {

        ArrayList<Task> historyListFromHistoryMap = new ArrayList<>();

        if (!linkedHistory.isEmpty()) {
            Node linkedTask = historyMap.get(headOfList.thisTask.getId());
            historyListFromHistoryMap.add(linkedTask.thisTask);

            while (linkedTask.nextTask != null) {    // linkedTask.thisTask.getId() != tailOfList.thisTask.getId()
                linkedTask = historyMap.get(linkedTask.nextTask.thisTask.getId());
                historyListFromHistoryMap.add(linkedTask.thisTask);
            }
        }


        return new ArrayList<>(historyListFromHistoryMap);

    }

    @Override
    public void add(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            linkedHistory.remove(historyMap.get(id));
            historyMap.remove(id);
        }
    }
}




