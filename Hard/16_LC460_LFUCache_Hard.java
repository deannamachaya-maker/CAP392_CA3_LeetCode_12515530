class LFUCache {

    class Node {
        int key;
        int value;
        int freq;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    HashMap<Integer, Node> map;
    HashMap<Integer, LinkedHashSet<Integer>> freqMap;
    int capacity;
    int minFreq;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        map = new HashMap<>();
        freqMap = new HashMap<>();
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }

        Node node = map.get(key);
        updateFreq(node);

        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            updateFreq(node);
            return;
        }

        if (map.size() == capacity) {
            LinkedHashSet<Integer> set = freqMap.get(minFreq);
            int evict = set.iterator().next();

            set.remove(evict);
            map.remove(evict);
        }

        Node node = new Node(key, value);
        map.put(key, node);

        freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);

        minFreq = 1;
    }

    private void updateFreq(Node node) {
        int freq = node.freq;

        LinkedHashSet<Integer> set = freqMap.get(freq);
        set.remove(node.key);

        if (set.isEmpty()) {
            freqMap.remove(freq);

            if (minFreq == freq) {
                minFreq++;
            }
        }

        node.freq++;

        freqMap.computeIfAbsent(node.freq, k -> new LinkedHashSet<>())
               .add(node.key);
    }
}