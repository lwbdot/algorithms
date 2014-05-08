package dot.study.hwtest5v3;

import java.util.ArrayList;

/**
 * 在二维矩阵中从给定的起点，找到是否有路径可以到达终点
 * 二维矩阵示例如下：
 * 12113
 * 10211
 * 21112
 * 其中矩阵维度不限，0表示起点，3表示终点，1表示可以走的方格，2表示无法穿越的障碍
 * @author llw
 *
 */
public class Main {
	public class Point{
		public int row; // 点的行坐标，即本点在第row行，row值为[0,矩阵行数-1]
		public int col; // 点的列坐标，即本点在第col列，col值为[0,矩阵列数-1]
		public Point(int y, int x){
			row = y;
			col = x;
		}
	}

	public class MapStack{
		public void push(Point p){  // 入栈
			if (null != p) {
				ps.add(p);
			}
		}
		public Point poop(){ // 弹出栈顶元素
			int top = ps.size()-1; // top = -1表示空栈
			if (top>=0) {
				Point point = ps.get(top);
				ps.remove(top);
				return point;
			}
			return null;
		}
		public Point visit(){ // 访问栈顶元素，但不删除栈顶元素
			int top = ps.size()-1;
			if (top>=0) {
				Point point = ps.get(top);
				return point;
			}
			return null;
		}
		public boolean isEmpty(){
			return ps.size()<1;
		}
	}

	public static ArrayList<Point> ps = new ArrayList<Point>();

	/**
	 * 采用深度优先遍历图（二维矩阵），不使用递归方式，而采用栈的方式
	 * 思路：首先找到出发点，出发点入栈，标记其已经被访问
	 *   0. 若栈不为空，则执行1，否则返回未找到从起点到终点的路径
	 *   1. 访问栈顶元素（但是不弹出栈顶元素），标记栈顶元素已经被访问
	 *
	 *   注意：下面的元素值只可能有4中情况，出发点已经被标记为访问过，所以它不会被再次访问到，故实际上只有3中访问情况
	 *   2. 若栈顶元素上方的点存在、未被访问过，并且可达（即值为1），则将上方元素入栈，标记其已被访问，执行0，否则执行3
	 *   3. 若栈顶元素上方的点存在、未被访问过，并且是终点（值为3），标记其已被访问，将其入栈，跳出循环，执行返回工作
	 *   4. 若栈顶元素上方的点存在、未被访问过，并且是障碍物（值为2），标记其已被访问，则执行5
	 *
	 *   5. 若栈顶元素右方的点存在、未被访问过，并且可达，则将右方元素入栈，标记其已经被访问，执行0，否则执行6
	 *   6. 若栈顶元素右方的点存在、未被访问过，并且是终点，标记其已被访问，将其入栈，跳出循环，执行返回工作
	 *   7. 若栈顶元素右方的点存在、未被访问过，并且是障碍物，标记其已被访问，则执行8
	 *
	 *   8. 若栈顶元素下方的点存在、未被访问过，并且可达，则将下方元素入栈，标记其已经被访问，执行0，否则执行9
	 *   9. 若栈顶元素下方的点存在、未被访问过，并且是终点，标记其已被访问，将其入栈，跳出循环，执行返回工作
	 *   10. 若栈顶元素下方的点存在、未被访问过，并且是障碍物，标记其已被访问，则执行11
	 *
	 *   11. 若栈顶元素左方的点存在、未被访问过，并且可达，则将左方元素入栈，标记其已经被访问，执行0，否则执行6
	 *   12. 若栈顶元素左方的点存在、未被访问过，并且是终点，标记其已被访问，将其入栈，跳出循环，执行返回工作
	 *   13. 若栈顶元素左方的点存在、未被访问过，并且是障碍物，标记其已被访问，则执行14
	 *   
	 *   14. 如果执行到这一步，说明当前节点周围都是不可访问的节点则此节点已经无任何用处，可以将其出栈抛弃了
	 *   15. 循环到此结束
	 *
	 * @param a 存放地图的二维数组，数组格式参考本类前面的注释
	 * @param rows 上面数组a的行数
	 * @param cols 上面数组a的列数
	 * @return 未找到，返回0，此时地图栈为空，即没有元素，找到则返回1，找到的路径在栈中
	 */
	public int searchMapDfsWithStack(int [][]a,int rows,int cols){
		int i = 0, j = 0;
		int[][] visited = new int[rows][cols]; // 记录某个节点是否被访问过，0表示没访问过，1表示访问过，其他值待定
		for (i = 0; i < rows; i++) {  // 初始时，每一个节点都未被访问过
			for(j = 0;j<cols;j++){
				visited[i][j] = 0;
			}
		}

		// 查找开始点的位置
		Point start = null;
		for (i = 0; i < rows; i++) {
			for(j = 0;j<cols;j++){
				if (a[i][j] == 0) {
					start = new Point(i, j);
					break;
				}
			}
		}

		MapStack s = new MapStack();
		s.push(start); // 将起始点压栈
		visited[start.row][start.col] = 1; // 起始点被压栈，则其已被访问
		Point top = null; // 栈顶节点
		boolean flag = false; // 是否存在从起点到终点的路径
		while (!s.isEmpty()) {
			top = s.visit();
			if (null == top) { // 空栈
				break;
			}

			// 向上访问
			int r = top.row-1, c = top.col; // 当前点（栈顶点）上方的点
			if ((r >= 0) && (visited[r][c] == 0)) { // 当前点不是第一行的点，并且上方的点未被访问过
				visited[r][c] = 1;
				if (a[r][c] == 1) { // 当前点上方的点可达
					s.push(new Point(r, c));
					continue;  // 不需要执行下面的代码了
				} else if (a[r][c] == 3) { // 当前上方的点是终点
					s.push(new Point(r, c));
					flag = true;
					break;
				} else if (a[r][c] == 2) { //当前点上方的点不可达
					// 什么也不做
				}
			}

			// 向右访问
			r = top.row;
			c = top.col+1;
			if (c<cols && (visited[r][c] == 0)) { // 当前点不是最后一列的点，并且其右边的点未被访问过
				visited[r][c] = 1;
				if (a[r][c] == 1) { // 当前点上方的点可达
					s.push(new Point(r, c));
					continue;  // 不需要执行下面的代码了
				} else if (a[r][c] == 3) { // 当前上方的点是终点
					s.push(new Point(r, c));
					flag = true;
					break;
				} else if (a[r][c] == 2) { //当前点上方的点不可达
					// 什么也不做
				}
			}

			// 向下访问
			r = top.row +1;
			c = top.col;
			if (r<rows && (visited[r][c] == 0)) { // 当前点不是最后一列的点，并且其右边的点未被访问过
				visited[r][c] = 1;
				if (a[r][c] == 1) { // 当前点上方的点可达
					s.push(new Point(r, c));
					continue;  // 不需要执行下面的代码了
				} else if (a[r][c] == 3) { // 当前上方的点是终点
					s.push(new Point(r, c));
					flag = true;
					break;
				} else if (a[r][c] == 2) { //当前点上方的点不可达
					// 什么也不做
				}
			}

			// 向左访问
			r = top.row;
			c = top.col-1;
			if ((c >= 0) && (visited[r][c] == 0)) { // 当前点不是第一行的点，并且上方的点未被访问过
				visited[r][c] = 1;
				if (a[r][c] == 1) { // 当前点上方的点可达
					s.push(new Point(r, c));
					continue;  // 不需要执行下面的代码了
				} else if (a[r][c] == 3) { // 当前上方的点是终点
					s.push(new Point(r, c));
					flag = true;
					break;
				} else if (a[r][c] == 2) { //当前点上方的点不可达
					// 什么也不做
				}
			}
			// 如果执行到这里，说明当前节点上下左右均不可达，则应该弹出当前节点
			s.poop();
		}

		// 输出从起点到终点的一条路径
		while (!s.isEmpty()) {
			Point point = s.poop();
			System.out.println(point.row+","+point.col);
		}
		if (flag) {
			return 1;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		int[][] a = {
				{1,1,1,2},
				{0,2,1,1},
				{2,2,2,1},
				{1,3,1,1}
		};
		int rows = 4,cols = 4;
		Main t = new Main();
		String s = "N";
		if(t.searchMapDfsWithStack(a, rows, cols)==1){
			s = "Y";
		}
		System.out.println(s);
	}

}
