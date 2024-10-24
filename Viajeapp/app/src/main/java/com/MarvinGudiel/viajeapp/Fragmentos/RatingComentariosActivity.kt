import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rating_comentarios.* // Si usas Fragment, cambia esto por ViewBinding

class RatingComentariosActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ComentariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_comentarios)

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance()

        // Configurar el RecyclerView
        adapter = ComentariosAdapter(listOf())
        recyclerViewComentarios.layoutManager = LinearLayoutManager(this)
        recyclerViewComentarios.adapter = adapter

        // Configurar el botón de enviar calificación y comentario
        btnEnviar.setOnClickListener {
            val rating = ratingBar.rating
            val comentario = etComentario.text.toString()

            if (comentario.isNotEmpty()) {
                enviarCalificacion(rating, comentario)
            } else {
                Toast.makeText(this, "Por favor, escribe un comentario", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar comentarios anteriores
        cargarComentarios()
    }

    // Función para enviar la calificación y comentario a Firestore
    private fun enviarCalificacion(rating: Float, comentario: String) {
        val calificacionMap = hashMapOf(
            "calificacion" to rating,
            "comentario" to comentario,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("calificaciones")
            .add(calificacionMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Calificación enviada", Toast.LENGTH_SHORT).show()
                ratingBar.rating = 0f
                etComentario.text.clear()
                cargarComentarios() // Actualizar los comentarios
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al enviar calificación: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para cargar comentarios anteriores
    private fun cargarComentarios() {
        firestore.collection("calificaciones")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                val comentarios = result.documents.map { document ->
                    Comentario(
                        document.getDouble("calificacion")?.toFloat() ?: 0f,
                        document.getString("comentario") ?: ""
                    )
                }
                adapter.updateComentarios(comentarios)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar comentarios: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
