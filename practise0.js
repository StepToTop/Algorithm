/**
 * 
 * @param {Array} numbers 
 * @param {int} len 
 * 数组中重复的数字
 * 时间复杂度O(n)
 * 会改变数组顺序
 */
function func1(numbers, len) {
  if (!(numbers instanceof Array) || (len <= 0)) {
    return false
  }
  for (let item of numbers) {
    if (item < 0 || item > len + 1) {
      return false
    }
  }
  for (let index in numbers) {
    while (numbers[index] !== index) {
      const temp_number = numbers[index]
      if (temp_number == numbers[temp_number]) {
        return true
      }
      numbers[index] = numbers[temp_number]
      numbers[temp_number] = temp_number;
    }
  }
  return false
}


let result = func1([2, 3, 1, 0, 2, 5, 3], 7)
console.log(result)



function func2(numbers, len) {
  
}

