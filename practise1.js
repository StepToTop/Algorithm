/**
 * 
 * @param {Array} arr_2_dim 
 * @param {Number} num 
 * 我的写法（左？下？左下？）
 */
function search_in_2_dim2(arr_2_dim, num) {
  if (!(arr_2_dim instanceof Array && arr_2_dim[0] instanceof Array) || num > arr_2_dim[arr_2_dim.length][arr_2_dim[0].length]) {
    return false, NaN, NaN;
  }
  row = arr_2_dim.length;
  col = arr_2_dim[0].length;
  i = 0;
  j = 0;
  while (num !== arr_2_dim[i][j]) {
    let row_move = 0, col_move = 0;
    if(i + 1 < row && num > arr_2_dim[i + 1][j]) 
      row_move += 1;
    if(j + 1 < col && num > arr_2_dim[i][j + 1]) 
      col_move += 1;
    if (row_move == 0 && col_move == 0) 
      return false, NaN, NaN;
    i += row_move;
    j += col_move;
  }
  return true, i, j;
}
/**
 * 牛逼的做法
 * 右上角比较
 * 往下走
 */



let result = search_in_2_dim2()
console.log()
