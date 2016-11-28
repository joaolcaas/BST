package adt.bst;

import java.util.ArrayList;
import java.util.List;

import adt.bt.BTNode;

public class BSTImpl<T extends Comparable<T>> implements BST<T> {

	protected BSTNode<T> root;

	public BSTImpl() {
		root = new BSTNode<T>();
	}

	public BSTNode<T> getRoot() {
		return this.root;
	}

	@Override
	public boolean isEmpty() {
		return root.isEmpty(); 
	}

	@Override
	public int height() {
		return height(this.root);
	}
	
	
	private int height(BSTNode<T> n) {
		if (n.isEmpty()) {
			return -1;
		} else {
			return 1 + Math.max(height((BSTNode<T>) n.getRight()), height((BSTNode<T>) n.getLeft()));
		}
	}

	
	@Override
	public BSTNode<T> search(T element) {
		if (element == null || this.root.isEmpty()) {
			return new BSTNode<T>();
		} else {
			return search(this.root, element); // fazer uma procura a partir da raiz
		}
	}

	private BSTNode<T> search(BSTNode<T> node, T element) {
		if (node.isEmpty() || node.getData().equals(element)) {
			return node;

		} else if ((node.getData().compareTo(element)) < 0) { //vai procurar a direita, se o elemento for maior que o nó
			return search((BSTNode<T>) node.getRight(), element);

		} else {
			return search((BSTNode<T>) node.getLeft(), element);// vai procurar a esquerda, se o elemento for menor que o nó
		}
	}

	@Override
	public void insert(T element) {
		if (element != null) {
			BSTNode<T> NIL = new BSTNode<T>();//o primeiro parent será um NIL, pois a raiz tem pai null
			insert(NIL, this.root, element);
		}
	}

	private void insert(BSTNode<T> parent, BSTNode<T> node, T element) {
		if (node.isEmpty()) {//se não tiver nada no nó, será inserido o valor do elemento nesse nó
			node.setData(element);
			node.setLeft(new BSTNode<T>());
			node.setRight(new BSTNode<T>());
			node.setParent(parent);

		} else {
			if ((node.getData().compareTo(element)) < 0) {
				insert(node, (BSTNode<T>) node.getRight(), element);
			} else {
				insert(node, (BSTNode<T>) node.getLeft(), element);
			}
		}
	}

	@Override
	public BSTNode<T> maximum() {
		if (this.root.isEmpty()) {
			return null;
		} else {
			return maximum(this.root);
		}
	}

	private BSTNode<T> maximum(BSTNode<T> node) {// vai ser o maximo para a direita
		if (node.getRight().isEmpty()) {
			return node;
		} else {
			return maximum((BSTNode<T>) node.getRight()); 
		}
	}

	@Override
	public BSTNode<T> minimum() {
		if (this.root.isEmpty()) {
			return null;
		} else {
			return minimum(this.root);
		}
	}

	private BSTNode<T> minimum(BSTNode<T> node) {//vai ser o maximo para a esquerda
		if (node.getLeft().isEmpty()) {
			return node;
		} else {
			return minimum((BSTNode<T>) node.getLeft()); 
		}
	}


	@Override
	public BSTNode<T> sucessor(T element) {

		BSTNode<T> nodeSearch = search(element);

		if (nodeSearch.isEmpty()) { 
			return null;

		} else {
			if (!nodeSearch.getRight().isEmpty()) {
				return minimum((BSTNode<T>) nodeSearch.getRight()); // se ele tiver direita, vai procurar o menor a direita de sua direita
			
			} else {
				return sucessor((BSTNode<T>) nodeSearch.getParent(), nodeSearch); // se ele não tiver direita, vai subindo a arvore enquanto ele for filho a direita
			}
		}
	}

	private BSTNode<T> sucessor(BSTNode<T> parent, BSTNode<T> node) {
		if ((parent != null) && ((!node.equals(parent.getLeft())))) {
			return sucessor((BSTNode<T>) parent.getParent(), (BSTNode<T>) parent);

		} else {
			return parent;
		}
	}

	@Override
	public BSTNode<T> predecessor(T element) {
		BSTNode<T> nodeSearch = search(element);

		if (element == null || nodeSearch.isEmpty()) {
			return null;

		} else {

			if (!nodeSearch.getLeft().isEmpty()) {//se ele tiver esquerda, vai pegar o maximo da esquerda da sua esquerda
				return maximum((BSTNode<T>) nodeSearch.getLeft()); 

			} else {
				return predecessor((BSTNode<T>) nodeSearch.getParent(), nodeSearch);// se ele não tiver esquerda, vai subindo a arvore enquanto ele for filho a esquerda
			}
		}
	}

	private BSTNode<T> predecessor(BSTNode<T> parent, BSTNode<T> node) {

		if ((parent != null) && ((!node.equals(parent.getRight())))) {
			return predecessor((BSTNode<T>) parent.getParent(), parent); 

		} else {
			return parent;
		}
	}

	@Override
	public void remove(T element) {
		BSTNode<T> node = search(element);
		if ((!node.isEmpty()) && (element != null)) {
			removeRecursive(node);
		}
	}

	private void removeRecursive(BSTNode<T> node) {
		if (node.getRight().isEmpty() && node.getLeft().isEmpty()) {// se não tiver nenhum filho, apenas "seta" os nós
			node.setData(null);
			node.setLeft(null);
			node.setRight(null);
		} else if (node.getRight().isEmpty()) {// se não tem direita, a esquerda sobe
			node.setData(node.getLeft().getData());
			node.setRight(node.getLeft().getRight());
			node.setLeft(node.getLeft().getLeft());
			node.getRight().setParent(node);
			node.getLeft().setParent(node);

		} else if (node.getLeft().isEmpty()) {//se não tem esquerda, a direita sobe
			node.setData(node.getRight().getData());
			node.setLeft(node.getRight().getLeft());
			node.setRight(node.getRight().getRight());
			node.getRight().setParent(node);
			node.getLeft().setParent(node);
		} else {
			T removed_node_value = node.getData();
			BTNode<T> sucessor = sucessor(removed_node_value);
			node.setData(sucessor.getData());
			sucessor.setData(removed_node_value);
			removeRecursive((BSTNode<T>) sucessor);
		}
	}

	@Override
	public T[] preOrder() {
		List<T> aux = new ArrayList<T>();
		//chamada para organizar.
		preOrder(this.root, aux);

		T[] array = (T[]) new Comparable[aux.size()];
		for (int i = 0; i < aux.size(); i++) {
			array[i] = aux.get(i);
		}
		return array;
	}

	private void preOrder(BSTNode<T> node, List<T> aux) {
		//RAIZ, ESRQUEDA , DIREITA
		if (!node.isEmpty()) {
			aux.add(node.getData());
			preOrder((BSTNode) node.getLeft(), aux);
			preOrder((BSTNode) node.getRight(), aux); 
		}
	}

	@Override
	public T[] order() {
		List<T> aux = new ArrayList<T>();
		order(this.root, aux);

		T[] array = (T[]) new Comparable[aux.size()];
		for (int i = 0; i < aux.size(); i++) {
			array[i] = aux.get(i);
		}
		return array;
	}

	private void order(BSTNode<T> node, List<T> aux) {
		//ESQUERDA, RAIZ , DIREITA
		if (!node.isEmpty()) {
			order((BSTNode) node.getLeft(), aux);
			aux.add(node.getData()); //RAIZ
			order((BSTNode) node.getRight(), aux); 
		}
	}

	@Override
	public T[] postOrder() {
		List<T> aux = new ArrayList<T>();
		postOrder(this.root, aux);

		T[] array = (T[]) new Comparable[aux.size()];
		for (int i = 0; i < aux.size(); i++) {
			array[i] = aux.get(i);
		}
		return array;
	}

	private void postOrder(BSTNode<T> node, List<T> aux) {
		//ESQUERDA, DIREITA, RAIZ
		if (!node.isEmpty()) {
			postOrder((BSTNode<T>) node.getLeft(), aux);
			postOrder((BSTNode<T>) node.getRight(), aux); 
			aux.add(node.getData()); 
		}
	}

	/**
	 * This method is already implemented using recursion. You must understand
	 * how it work and use similar idea with the other methods.
	 */
	@Override
	public int size() {
		return size(root);
	}

	private int size(BSTNode<T> node) {
		int result = 0;
		// base case means doing nothing (return 0)
		if (!node.isEmpty()) { // indusctive case
			result = 1 + size((BSTNode<T>) node.getLeft()) + size((BSTNode<T>) node.getRight());
		}
		return result;
	}
}