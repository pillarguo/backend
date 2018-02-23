package cmdc.bean;


import java.util.ArrayList;
import java.util.List;

/** 
 * Description: 分页 
 * @author 陈家骏
 * 2017-3-13
 */
public class Page<E> {

	private int pageNum;   //当前页码
    private int pageSize;  //每页行数
    private int startRow;  //当前开始行 
    private long total;  //总行数
    private int pages;  //总页数
    private List<E> result=new ArrayList<E>();  //查询结果
    
    public Page(int pageNum, int pageSize) {  
        this.pageNum = pageNum;  
        this.pageSize = pageSize;  
        this.startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;  
    }  

	public List<E> getResult() {  
        return result;  
    }  

    public void setResult(List<E> result) {  
        this.result = result;  
    }  

    public int getPages() {  
        return pages;  
    }  

    public void setPages(int pages) {  
        this.pages = pages;  
    }  

    public int getPageNum() {  
        return pageNum;  
    }  

    public void setPageNum(int pageNum) {  
        this.pageNum = pageNum;  
    }  

    public int getPageSize() {  
        return pageSize;  
    }  

    public void setPageSize(int pageSize) {  
        this.pageSize = pageSize;  
    }  

    public int getStartRow() {  
        return startRow;  
    }  

    public void setStartRow(int startRow) {  
        this.startRow = startRow;  
    }  

    public long getTotal() {  
        return total;  
    }  

    public void setTotal(long total) {  
        this.total = total;  
    }  

  /*  @Override  
    public String toString() {  
        return "Page{" +  
                "pageNum=" + pageNum +  
                ", pageSize=" + pageSize +  
                ", startRow=" + startRow +  
                ", total=" + total +  
                ", pages=" + pages +  
                '}';  
    } */ 
}
