import java.util.*;
  /**
   * Lab 3 -- Bank Simulation
   * Description: This code uses established values(such as arrival time,
   * customer number, ect.) to determine the time required for tellers from
   * 2 to 10 to go through all of the customers in a simulation fashion.
   * Written By: Ethan Bown 
   * Made in: Java
   * Finished: 4/11/22
   * Last Editied: 9/28/23
   */
public class banksim {
  public static void main(String[] args) { //Starts the simulation for tellers = 2 all the way to tellers = 10
    banksim sim = new banksim();
    for (int i = 2; i < 11; i++) {
      sim.make_tellers(i);
    }
  }

  public class node<T> {
    public T data;
    public node<T> next; 
    public node(T nodedata) {
      data = nodedata;
      next = null;
    }
  }

  public class Header<T> {
    private node<T> top;
    public Header() {
      top = null;
    }
    public void Push(T data) { // Adds data to Linked List
      node<T> NN = new node<T>(data);
      if (top == null) {
        top = NN;
      } else {
        NN.next = top;
        top= NN;
      }
    }
    public T Pop(T data) { // Removes data from Linked List
      if (top == null) {
        throw new NoSuchElementException();
      } else {
        data = top.data;
        top = top.next;
      }
      return data;
    }
  }

  public void make_tellers(int amount) { // Creates Array of Teller Linked Lists
    banksim make = new banksim();
    @SuppressWarnings("unchecked")
    Header<Integer>[] tellers = new Header[amount]; // Main -- Simulates a customer being assigned to a teller
    Integer[] fortells = new Integer[amount]; // Secondary -- Holds the data from the customer, which is their transaction time
    for (int b = 0; b < amount; b++) {
      Header<Integer> temp = make.new Header<>();
      tellers[b] = temp;
    }
    Simulation(amount, tellers, fortells);
  }  

  public void Simulation(int amount, Header<Integer>[] tellers, Integer[] fortells) {
    banksim banksim = new banksim();
    Integer[] arrival_time = // Holds all of the arrival times of the customer's in descending order
    { 35, 33, 33, 32, 31, 30, 29, 28, 28, 28,
      27, 27, 26, 25, 25, 24, 24, 24, 22, 22,
      22, 21, 19, 17, 17, 17, 16, 15, 14, 13,
      11, 10, 10, 10, 10, 10, 9,  8,  7,  6,
      4,  4,  2,  2,  1,  1,  0,  0,  0,  0 };
    Header<Integer> bank_line = banksim.new Header<>(); // Holds all of the arrival times on one LL, in order from least to greatest
    for (int q = 0; q < 50; q++) {
      bank_line.Push(arrival_time[q]);
    }
    Header<Integer> customer_numbers = banksim.new Header<>(); // LL that holds customer numbers from 1 to 50
    for (int q = 50; q > 0; q--) {
      customer_numbers.Push(q);
    }
    int[] transaction_time = // Holds all of the transaction time data
    { 2, 2, 1, 4, 1, 7, 3, 8, 2, 4,
      10, 3, 5, 1, 4, 1, 5, 7, 3, 9, 
      14, 11, 3, 3, 1, 5, 4, 6, 1, 3, 
      6, 1, 2, 2, 4, 1, 2, 1, 5, 1, 
      7, 1, 2, 1, 8, 1, 4, 1, 1, 2 };
    int current_time, process_time, patron_number, sum;
    current_time = process_time = 0;
    patron_number = sum = 1;
    int[] transaction_copy = // Copy of transaction time since original will be modified
    { 2, 2, 1, 4, 1, 7, 3, 8, 2, 4,
      10, 3, 5, 1, 4, 1, 5, 7, 3, 9, 
      14, 11, 3, 3, 1, 5, 4, 6, 1, 3, 
      6, 1, 2, 2, 4, 1, 2, 1, 5, 1, 
      7, 1, 2, 1, 8, 1, 4, 1, 1, 2 };
    while (bank_line.top != null || sum != 0) { // Only stops running the sim when there is no more customers or when all numbers in transaction_time = 0 (what sum is)
      sum = 0;
      for (int p = 0; p < 50; p++) { // Adds all numbers in the array, if all numbers = 0 then all customers have been processed
        sum+= transaction_time[p];
      }
      for (int w = 0; w < amount; w++) { 
        if (tellers[w] != null && current_time >= arrival_time[Math.abs(patron_number - 50)]) { // Only runs when tellers is not null and when at least 1 customer has arrived and needs service
          try {
            if (bank_line.top != null && tellers[w].top == null) { // Adds new customer to line if tellers[w] is empty and there are still customers
              bank_line.Pop(arrival_time[Math.abs(patron_number - 50)]);
              tellers[w].Push(customer_numbers.Pop(patron_number));
              patron_number++;
            } else if (bank_line.top != null && tellers[w].top.data == -1) { // Adds new customer to line if tellers[w] is empty (what -1 means) and there are still customers
              tellers[w].Pop(-1);
              bank_line.Pop(arrival_time[Math.abs(patron_number - 49)]);
              tellers[w].Push(customer_numbers.Pop(patron_number));
              patron_number++;
            } else if (bank_line.top == null && tellers[w].top.data == -1) { // Doesn't add new customer to line if there are no more customers to add
              tellers[w].Pop(-1);
              tellers[w].Push(100);
            }
          } 
          catch (Exception e) { // Runs when there is no tellers[w].top.data to pull
            continue;
          }
        }
      }
      for (int b = 0; b < amount; b++) { // Processes each customer in line, removing 1 from the transaction time
        if (tellers[b].top != null) { // Adds data to fortells[b] for ease of use
          fortells[b] = tellers[b].top.data;
        }
      }
      for (int v = 0; v < amount; v++) {
        if (fortells[v] == null || fortells[v] == 100 || fortells[v] == -1) {
          continue;
        }
        if (transaction_time[fortells[v] - 1] == 0) {
          continue;
        }
        transaction_time[fortells[v] - 1]--; // Removes 1 from specific customer's transaction time and lets time be incremented later
      }
      current_time += 1;
      for (int e = 0; e < amount; e++) { // Process out customers at 0 process time, add finshed customers to a list, and increment process time
        if (fortells[e] != null ) {
          if (fortells[e] != 100 && fortells[e] != -1) { //Previous "if" statements prevent errors
            if (transaction_time[fortells[e]-1] == 0) { // Calculates process time using current time, arrival time, and the untouched transaction time
              tellers[e].Pop(fortells[e]);
              tellers[e].Push(-1);
              process_time += (current_time - arrival_time[Math.abs(fortells[e] - 50)]) + transaction_copy[fortells[e] - 1];
            }
          }
        }
      }
    }
    float pt_copy = process_time; // Calculates average process time once simulation is done
    float running_total =  pt_copy / 50;
    System.out.println("Average Process Time with " +  amount + " Tellers -- " + running_total);
    System.out.println("Process Time Total -- " + process_time);
  }     
}