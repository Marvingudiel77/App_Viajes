import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

data class Comentario(val calificacion: Float, val comentario: String)

class ComentariosAdapter(private var comentarios: List<Comentario>) :
    RecyclerView.Adapter<ComentariosAdapter.ComentariosViewHolder>() {

    class ComentariosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val comentarioText: TextView = view.findViewById(R.id.tvComentario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentariosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comentario, parent, false)
        return ComentariosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComentariosViewHolder, position: Int) {
        val comentario = comentarios[position]
        holder.ratingBar.rating = comentario.calificacion
        holder.comentarioText.text = comentario.comentario
    }

    override fun getItemCount(): Int = comentarios.size

    // Función para actualizar los comentarios cuando se carguen nuevos desde Firestore
    fun updateComentarios(nuevosComentarios: List<Comentario>) {
        comentarios = nuevosComentarios
        notifyDataSetChanged()
    }
}
