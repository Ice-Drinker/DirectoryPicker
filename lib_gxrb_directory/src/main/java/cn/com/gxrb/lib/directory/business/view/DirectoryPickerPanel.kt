package cn.com.gxrb.lib.directory.business.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.com.gxrb.lib.directory.R
import cn.com.gxrb.lib.directory.api.IDirectoryBean
import cn.com.gxrb.lib.directory.business.view.widget.RbAdapterViewHolder
import cn.com.gxrb.lib.directory.common.DisplayUtils
import kotlinx.android.synthetic.main.panel_directory_picker.view.*
import java.util.*

class DirectoryPickerPanel : FrameLayout {

    private lateinit var wholeDirectoryBean: IDirectoryBean  //完整的目录数据，包括跟节点
    private lateinit var directoryAdapter: DirectoryAdapter

    private var showDirectories: MutableList<IDirectoryBean> = ArrayList()  //当前正在显示的目录
    val checkedDirectories = ArrayList<IDirectoryBean>()    //选中的目录
    var folderCheckable = false    //父节点是否可以选择
    var fileCheckable = false  //子节点是否可以选择
    private var onItemClick: ((IDirectoryBean, Boolean) -> Unit)? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        View.inflate(context, R.layout.panel_directory_picker, this)
    }

    fun initData(directoryBean: IDirectoryBean) {
        this.wholeDirectoryBean = directoryBean

        val rootText = makeDirectoryText(wholeDirectoryBean)
        val separatorView = makeSeparatorView()
        ll_path.addView(rootText)
        ll_path.addView(separatorView)

        showDirectories.addAll(directoryBean.directories)
        directoryAdapter = DirectoryAdapter(showDirectories)
        lv_directory.adapter = directoryAdapter
    }

    private inner class DirectoryAdapter(val directories: MutableList<IDirectoryBean>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val holder: RbAdapterViewHolder = RbAdapterViewHolder.get(context, convertView, parent, R.layout.ui_directory_picker_item, position)
            val cb_check = holder.getView<CheckBox>(R.id.cb_checked)
            val tv_name = holder.getView<TextView>(R.id.tv_name)

            val bean = directories[position]
            cb_check.isChecked = isChecked(bean)
            tv_name.text = bean.name

            //节点是否可以选择
            if (bean.isFolder) {
                cb_check.visibility = if (folderCheckable) View.VISIBLE else View.GONE
                tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.directory_right_arrow, 0)
            } else {
                cb_check.visibility = if (fileCheckable) View.VISIBLE else View.GONE
                tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }

            //处理点击事件
            cb_check.setOnClickListener {
                holder.convertView.performClick()
            }
            holder.convertView.setOnClickListener {
                if (bean.isFolder) {    //父节点
                    showDirectories.clear()
                    showDirectories.addAll(bean.directories)
                    notifyDataSetChanged()

                    //处理顶部路径
                    val separatorView = makeSeparatorView()
                    val directoryTextView = makeDirectoryText(bean)
                    ll_path.addView(directoryTextView)
                    ll_path.addView(separatorView)
                }
                if (cb_check.visibility == View.VISIBLE) {
                    if (isChecked(bean)) {
                        unchecked(bean)
                        cb_check.isChecked = false
                    } else {
                        checked(bean)
                        cb_check.isChecked = true
                    }
                }
                onItemClick?.invoke(bean, cb_check.isChecked)
            }
            return holder.convertView
        }

        override fun getItem(position: Int): Any {
            return directories[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return directories.size
        }

    }

    private fun checked(directoryBean: IDirectoryBean) {
        if (directoryBean.isFolder && folderCheckable) {
            checkedDirectories.add(directoryBean)
        }
        if (!directoryBean.isFolder && fileCheckable) {
            checkedDirectories.add(directoryBean)
        }
    }

    private fun unchecked(directoryBean: IDirectoryBean) {
        checkedDirectories.remove(directoryBean)
    }

    private fun isChecked(directoryBean: IDirectoryBean): Boolean {
        checkedDirectories.forEach {
            if (it.id == directoryBean.id) {
                return true
            }
        }
        return false
    }


    private fun makeSeparatorView(): ImageView {
        val separatorView = ImageView(context)
        separatorView.setImageResource(R.drawable.directory_right_arrow)
        val lp =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER_VERTICAL
        lp.marginEnd = DisplayUtils.dip2px(context, 6F)
        separatorView.layoutParams = lp
        return separatorView
    }

    private fun makeDirectoryText(directoryBean: IDirectoryBean): TextView {
        val textView = TextView(context)
        textView.tag = directoryBean
        textView.text = directoryBean.name
        textView.setTextColor(context.resources.getColor(R.color.directory_path_text_gray))
        val leftPadding = DisplayUtils.dip2px(context, 6F)
        val topPadding = DisplayUtils.dip2px(context, 10F)
        textView.setPadding(0, topPadding, leftPadding, topPadding)

        textView.setOnClickListener {
            var index = ll_path.indexOfChild(textView)
            val count = ll_path.childCount
            if (index < count) {
                index += 2
                for (x in count downTo index) {    //从下一个箭头开始，全部移除
                    ll_path.removeView(ll_path.getChildAt(x))
                }
            }

            showDirectories.clear()
            showDirectories.addAll(directoryBean.directories)
            directoryAdapter.notifyDataSetChanged()
        }
        return textView
    }

    fun setOnItemClickListener(onItemClickListener: ((IDirectoryBean, Boolean) -> Unit)) {
        this.onItemClick = onItemClickListener
    }
}