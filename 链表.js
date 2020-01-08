class ListNode {
  constructor(x) {
    this.val = x;
    this.next_node = null;
  }
}
//练习1 生成链表
function practise_1() {
  let a = new ListNode(10);
  let b = new ListNode(20);
  let c = new ListNode(30);
  let d = new ListNode(40);
  let e = new ListNode(50);
  let f = new ListNode(60);
  let g = new ListNode(70);
  a.next_node = b;
  b.next_node = c;
  c.next_node = d;
  d.next_node = e;
  e.next_node = f;
  f.next_node = g;
  head = a;
  // while(head) {
  //   console.log(`当前值为${head.val}`)
  //   head = head.next_node;
  // }
  return head
}
function practise_2() {
  let a = new ListNode(40);
  let b = new ListNode(50);
  let c = new ListNode(60);
  let d = new ListNode(70);
  let e = new ListNode(80);
  let f = new ListNode(90);
  let g = new ListNode(100);
  a.next_node = b;
  b.next_node = c;
  c.next_node = d;
  d.next_node = e;
  e.next_node = f;
  f.next_node = g;
  g.next_node = d; 
  head = a;
  // while(head) {
  //   console.log(`当前值为${head.val}`)
  //   head = head.next_node;
  // }
  return head
}
// practise_1()
/**
 * 例子1
 * 链表逆序
 * 方法1：就地逆置法
 * 方法2：头插法
 */
// solution_1();
class Solution {
  constructor(head) {
    this.head = head
  }
  reverseList() {
    let new_head = null;
    let next = null;
    while(this.head) {
      next = this.head.next_node;
      this.head.next_node = new_head;
      new_head = this.head;
      this.head = next;
    }
    this.head = new_head
  }
  reverseList1() {
    let temp_head = new ListNode(null);
    let next = null;
    while(this.head != null) {
      next = temp_head.next_node;
      temp_head.next_node = this.head;
      this.head = this.head.next_node;
      temp_head.next_node.next_node = next;
    }
    this.head = temp_head.next_node;
  }
  reverseBetween(m, n) {
    let chang_len = n - m + 1;  // 逆置个数
    let pre_head = null;  // 判断是否有前驱
    let result = this.head;  // 暂时存储
    while (this.head && --m) {
      pre_head = this.head;
      this.head = this.head.next_node;
    }
    let modify_list_tail = this.head;
    let new_head = null;
    while(this.head && chang_len) {
      let next = this.head.next_node;
      this.head.next_node = new_head;
      new_head = this.head;
      this.head = next;  // 跳出去了
      chang_len --;
    }
    modify_list_tail.next_node = this.head;
    if (pre_head) {
      pre_head.next_node = new_head
    }else {
      result = new_head;
    }
    this.head = result;
    return result;
  }
  mergeLinks(link1, link2) {
    let temp_head = new ListNode(null);
    let head = temp_head;
    while(link1 && link2) {
      if (link1.val <= link2.val) {
        temp_head.next_node = link1;
        link1 = link1.next_node;
      }else {
        temp_head.next_node = link2;
        link2 = link2.next_node;
      }
      temp_head = temp_head.next_node;
    }
    if (link1) {
      temp_head.next_node = link1
    }
    if (link2) {
      temp_head.next_node = link2;
    }
    return head.next_node
  }
  partition(head, x) {

  }
  getIntetsectionNode(headA, headB) {
    /**
     * 方法一
     * 空间复杂度O(n)
     */
    node_set = new Set();
    while (headA) {
      node_set.add(headA);
      headA = headA.next_node;
    }
    while(headB) {
      if(node_set.has(headB)) {
        return headB;
      }
      headB = headB.next_node;
    }
    return null;
    /**
     * 方法二
     * 空间复杂度O（1）
     */

  }
  detectCycle(head) {

  }
  check(head) {
    let temp_head = head;
    let result = "";
    while(temp_head) {
      result = result + temp_head.val.toString() + " ";
      temp_head = temp_head.next_node;
    }
    console.log(result);
  }
  getListLength(link) {
    let len = 0;
    while(link) {
      len ++;
      link = link.next_node;
    }
    return len; 
  }
  forward_long_list(long, short, head) {
    let len = long - short;
    while(head && len --) {
      head = head.next_node;
    }
    return head;
  }
}
function solution_1() {
  let test_list_head = practise_1();
  let solution = new Solution(test_list_head);
  solution.check();
  solution.reverseList();
  solution.check();
  solution.reverseList1();
  solution.check()
}
/**
 * 例子2
 * 链表中间段逆序
 * *边界条件 *中间段四个点
 * 
 */
// solution_2()
// solution_2()
function solution_2() {
  let test_list_head = practise_1();
  let solution = new Solution(test_list_head);
  solution.check()
  // solution.reverseBetween(3, 5);
  solution.reverseBetween(1, 5);
  solution.check()
}
/**
 * 例子3
 * 链表合并
 */
solution_3();
function solution_3() {
  let test_list_head = practise_1();
  let test_list_head_1 = practise_2();
  let solution = new Solution();
  let result = solution.mergeLinks(test_list_head, test_list_head_1);
  solution.check(result);
}
/**
 * 例子4
 * 寻找两个链表的交点
 * 方法一：使用set存储地址
 * 方法二：对齐，头部对齐
 */
/**
 * 例子5
 * 链表求环
 * 方法1：Set
 * 方法2：快慢指针赛跑（约瑟夫）只能知道有环（空间O1）
 */
/**
 * 例子6
 * 链表划分
 * 临时头节点
 */
/**
 * 例子7
 * 复杂链表的深拷贝
 * 方法：Map映射下标
 */