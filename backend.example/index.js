import express from "express";
const app = express();
const port = 3000;

// Rute dasar (Halaman Utama)
/**
app.get("/", (req, res) => {
    res.send("Hello World dari Express.js!");
});
*/
// Contoh rute lain
app.get("/api/ai", (req, res) => {
    try {
        const { prompt } = req.body;

        // Validasi input jika prompt kosong
        if (!prompt) {
            return res.status(400).json({
                code: 400,
                text: 'Harap masukkan kolom "prompt" di dalam body JSON.',
                timestamp: Date.now()
            });
        }

        const response =
            prompt == "Hi" ? "Halo juga coyy" : "Ini Adalah Respon ai";
        res.json({
            code: 200,
            text: response,
            timestamp: Date.now()
        });
    } catch (error) {
        console.error("Error AI:", error);
        res.status(500).json({
            code: 500,
            text: "Terjadi kesalahan pada server AI.",
            timestamp: Date.now()
        });
    }
});

app.listen(port, () => {
    console.log(`Express berjalan di http://localhost:${port}`);
});
