package cc.ayakurayuki.reminder.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.media.RingtoneManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.ayakurayuki.reminder.R
import java.util.*

/**
 * Created by ayakurayuki on 2017/12/27.
 */
class SetRingtoneAdapter(var context: Context) : RecyclerView.Adapter<SetRingtoneAdapter.ListViewHolder>(), View.OnClickListener {

    private val ringtoneList: MutableList<String> = ArrayList()
    private var listener: OnRecyclerViewItemClickListener? = null

    init {
        val ringtoneManager = RingtoneManager(context)
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE)
        val cursor = ringtoneManager.cursor
        if (cursor.moveToFirst()) {
            do {
                ringtoneList.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX))
                println(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX))
            } while (cursor.moveToNext())
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetRingtoneAdapter.ListViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.ringtone_list, null).apply {
            setOnClickListener(this@SetRingtoneAdapter)
        }
        return ListViewHolder(v)
    }

    override fun onBindViewHolder(holder: SetRingtoneAdapter.ListViewHolder, position: Int) {
        holder.ringtonePath.text = ringtoneList[position]
        holder.itemView.setTag(R.id.tag_first, ringtoneList[position])
        holder.itemView.setTag(R.id.tag_second, position)
    }

    override fun getItemCount(): Int {
        return ringtoneList.size
    }

    override fun onClick(v: View) {
        if (listener != null) {
            // 注意这里使用getTag方法获取数据
            listener!!.onItemClick(v, v.getTag(R.id.tag_first) as String, v.getTag(R.id.tag_second) as Int)
        }
    }

    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ringtonePath: TextView = itemView.findViewById(R.id.tone_name) as TextView
    }

    //点击事件接口
    interface OnRecyclerViewItemClickListener {
        fun onItemClick(view: View, data: String, position: Int)
    }

}
