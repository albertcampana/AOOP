package intSet;

/**
 * Representation of a finite set of integers.
 * 
 * '@'invariant getCount() greater or equal 0
 * '@'invariant getCount() less or equal than getCapacity()
 */
public class IntSet {

	private int capacity;
	private int[] intSet;
	private int count;

	/**
	 * Creates a new set with 0 elements.
	 * 
	 * @param capacity
	 *            the maximal number of elements this set can have
	 * '@'pre capacity greater or equal than 0
	 * '@'post getCount() == 0
	 * '@'post getCapacity() == capacity
	 */
	public IntSet(int capacity) throws Exception {

		if (capacity < 0) throw new Exception ("capacity < 0") ;

		this.capacity = capacity;
		this.count = 0;
		this.intSet = new int[capacity];
	}

	/**
	 * Test whether the set is empty.
	 * 
	 * @return getCount() == 0
	 */
	public boolean isEmpty() {
		return this.count == 0;
	}

	/**
	 * Test whether a value is in the set
	 * 
	 * @return true if there exists an int v in getArray() such that v == value
	 */
	public boolean has(int value) {
		for(int i = 0; i < count; i++){
			if(intSet[i] == value) return true;
		}
		return false;
	}

	/**
	 * Adds a value to the set.
	 * 
	 * '@'pre getCount() less than getCapacity()
	 * '@'post has(value)
	 * '@'post !this@pre.has(value) implies (getCount() == this@pre.getCount() + 1)
	 * '@'post this@pre.has(value) implies (getCount() == this@pre.getCount())
	 */
	public void add(int value) throws Exception {
		if(this.count < this.capacity){
			if(!this.has(value)){
				intSet[count++] = value;
			}else{
				throw new Exception ("Repeated value");
			}
		}else{
			throw new Exception ("Set is full");
		}
	}

	/**
	 * Removes a value from the set.
	 * 
	 * '@'post !has(value)
	 * '@'post this@pre.has(value) implies (getCount() == this@pre.getCount() - 1)
	 * '@'post !this@pre.has(value) implies (getCount() == this@pre.getCount())
	 */
	public void remove(int value) {
		if(this.count > 0){
			int[] intSet_aux = new int[capacity];
			int count_aux = this.count;
			for(int i = 0, k = 0; i < this.count; i++){
				if(intSet[i] != value) {
					intSet_aux[k++] = intSet[i];
				}
				else count_aux--;
			}
			this.intSet = intSet_aux;
			this.count = count_aux;
		}
	}

	/**
	 * Returns the intersection of this set and another set.
	 * 
	 * @param other
	 *            the set to intersect this set with
	 * @return the intersection
	 * '@'pre other != null
	 * '@'post forall int v: (has(v) and other.has(v)) implies return.has(v)
	 * '@'post forall int v: return.has(v) implies (has(v) and other.has(v))
	 */
	public IntSet intersect(IntSet other) throws Exception {
		if(other == null) throw new Exception ("Array is null");

		int intersect_capacity = Math.min(this.capacity, other.capacity);
		IntSet intersection = new IntSet(intersect_capacity);

		for(int i = 0; i < this.count; i++){
			if(other.has(this.intSet[i])){
				intersection.add(this.intSet[i]); //We add the value
			}
		}

		return intersection;
	}

	/**
	 * Returns the union of this set and another set.
	 *
	 * @param other
	 *            the set to union this set with
	 * @return the union
	 * '@'pre other != null
	 * '@'post forall int v: has(v) implies return.has(v)
	 * '@'post forall int v: other.has(v) implies return.has(v)
	 * '@'post forall int v: return.has(v) implies (has(v) or other.has(v))
	 */
	public IntSet union(IntSet other) throws Exception {
		if(other == null) throw new Exception ("Array is null");

		int union_capacity = other.capacity + this.capacity;

		IntSet union = new IntSet(union_capacity);

		for(int i = 0; i < this.count; i++){
			union.add(this.intSet[i]);
		}

		for(int i = 0; i < other.count; i++){
			if (!union.has(other.intSet[i])) union.add(other.intSet[i]);
		}

		return union;
	}

	/**
	 * Returns the difference of this set and another set.
	 *
	 * @param other
	 *            the set to difference this set with
	 * @return the difference
	 * '@'pre other != null
	 * '@'post forall int v: return.has(v) implies (has(v) and !other.has(v))
	 */
	public IntSet difference(IntSet other) throws Exception {
		if(other == null) throw new Exception ("Array is null");

		int difference_capacity = this.capacity;
		IntSet difference = new IntSet(difference_capacity);

		for(int i = 0; i < this.count; i++){
			if(!other.has(this.intSet[i])){
				difference.add(this.intSet[i]); //We add the value
			}
		}

		return difference;
	}

	/**
	 * Returns the symmetric difference of this set and another set.
	 *
	 * @param other
	 *            the set to symmetric difference this set with
	 * @return the symmetric difference
	 * '@'pre other != null
	 * '@'post forall int v: return.has(v) implies (has(v) and !other.has(v))
	 * '@'post forall int v: return.has(v) implies (!has(v) and other.has(v))
	 */
	public IntSet symmetricDifference(IntSet other) throws Exception {
		if(other == null) throw new Exception ("Array is null");

		int sym_diff_capacity = this.capacity + other.capacity;
		IntSet sym_diff = new IntSet(sym_diff_capacity);

		for(int i = 0; i < this.count; i++){
			if(!other.has(this.intSet[i])){
				sym_diff.add(this.intSet[i]); //We add the value
			}
		}

		for(int i = 0; i < other.count; i++){
			if(!this.has(other.intSet[i])){
				sym_diff.add(other.intSet[i]); //We add the value
			}
		}

		return sym_diff;
	}

	/**
	 * Returns a representation of this set as an array
	 *
	 * @return an array containing all elements of the set
	 * '@'post return.length == getCount()
	 * '@'post forall int v in return: has(v)
	 */
	public int[] getArray() {
		return this.intSet;
	}

	/**
	 * Returns the number of elements in the set.
	 *
	 * @return the number of elements in the set
	 */
	public int getCount() {

		return count;
	}

	/**
	 * Returns the maximal number of elements in the set.
	 *
	 * @return the capacity of the set
	 */
	public int getCapacity() {

		return capacity;
	}

	/**
	 * Returns a string representation of the set. The empty set is represented
	 * as {}, a singleton set as {x}, a set with more than one element like {x,
	 * y, z}.
	 *
	 * @return string representation of this set
	 */
	public String toString() {

		String output = "{";
		if (this.count>0) output += (char)(this.intSet[0]+'0');
		for (int i = 1; i < this.count; i++){
			output += ",";
			output += (char)(this.intSet[i]+'0');
		}
		output += "}";

		return output;
	}

}
