package otherutils;

/**
 * Created by Administrator on 2017/10/30.
 * 用来将两个Array数组里面的元素按有序的方式重新排序(赋予)到一个数组中去
 */

public class OrderArray {
    /**将数组nums1 数组nums2 重新排序再放入nums1中
     * @param nums1 数组1
     * @param m     数组1长度
     * @param nums2 数组2
     * @param n     数组2长度
     */
    public static void merge(int nums1[], int m, int nums2[], int n) {
        if (nums1 == null || nums2 == null)
            return;
        int idx1 = m - 1;
        int idx2 = n - 1;
        int len = m + n - 1;
        while (idx1 >= 0 && idx2 >= 0) {
            if (nums1[idx1] > nums2[idx2]) {
                nums1[len--] = nums1[idx1--];
            } else {
                nums1[len--] = nums2[idx2--];
            }
        }
        while (idx2 >= 0) {
            nums1[len--] = nums2[idx2--];
        }
    }
}
