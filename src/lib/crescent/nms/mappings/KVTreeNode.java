package lib.crescent.nms.mappings;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class KVTreeNode<K, V> {
	public K key;
	public V value;
	private KVTreeNode<K, V> parent = null;
	private int depth = 1;
	private HashMap<K, KVTreeNode<K, V>> children = null;

	public KVTreeNode() {

	}

	public KVTreeNode(K key) {
		this(key, null, null);
	}

	public KVTreeNode(K key, V value) {
		this(key, value, null);
	}

	public KVTreeNode(K key, KVTreeNode<K, V> parent) {
		this(key, null, parent);
	}

	public KVTreeNode(K key, V value, KVTreeNode<K, V> parent) {
		this.key = key;
		this.value = value;
		if (parent != null)
			attachTo(parent);
	}

	public KVTreeNode<K, V> attachChild(KVTreeNode<K, V> child) {
		child.parent = this;
		child.depth = depth + 1;
		if (children == null)
			children = new HashMap<>();
		children.put(child.key, child);
		return this;
	}

	public KVTreeNode<K, V> attachChild(K child_key, V child_value) {
		return attachChild(new KVTreeNode<K, V>(child_key, child_value));
	}

	public KVTreeNode<K, V> attachTo(KVTreeNode<K, V> parent) {
		this.parent = parent;
		depth = parent.depth + 1;
		if (parent.children == null)
			parent.children = new HashMap<>();
		parent.children.put(key, this);
		return this;
	}

	public KVTreeNode<K, V> getParent() {
		return parent;
	}

	public KVTreeNode<K, V> getChild(K key) {
		if (children == null)
			return null;
		return children.get(key);
	}

	@SuppressWarnings("unchecked")
	public KVTreeNode<K, V> findChildChainNode(K[] keys, boolean create_if_not_exist) {
		int residual_keys_len = keys.length - 1;
		K[] residual_keys = null;
		if (residual_keys_len > 0) {
			residual_keys = (K[]) new Object[residual_keys_len];
			System.arraycopy(keys, 1, residual_keys, 0, residual_keys_len);
		} else
			return new KVTreeNode<K, V>(keys[0], this);// 最后一个节点
		if (children == null || (!children.containsKey(keys[0]))) {
			if (create_if_not_exist)
				attachChild(new KVTreeNode<K, V>(keys[0]));
			else
				return null;
		}
		KVTreeNode<K, V> child = children.get(keys[0]);
		return child.findChildChainNode(residual_keys, create_if_not_exist);
	}

	public int getDepth() {
		return depth;
	}

	public KVTreeNode<K, V>[] getChildren() {
		if (children == null)
			return null;
		@SuppressWarnings("unchecked")
		KVTreeNode<K, V>[] c = new KVTreeNode[children.size()];
		Set<Entry<K, KVTreeNode<K, V>>> children_set = children.entrySet();
		int idx = 0;
		for (Entry<K, KVTreeNode<K, V>> child : children_set) {
			c[idx] = child.getValue();
			++idx;
		}
		return c;
	}

	@Override
	public String toString() {
		return key.toString() + ": " + value.toString();
	}
}
