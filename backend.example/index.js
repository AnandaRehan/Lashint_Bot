import express from "express";
const app = express();
const port = 3000;

app.use(express.json());
app.use(express.text({ type: "*/*" }));
// Rute dasar (Halaman Utama)
/**
app.get("/", (req, res) => {
    res.send("Hello World dari Express.js!");
});
*/
// Contoh rute lain

app.post("/api/ai", async (req, res) => {
    try {
        if (!req.body) {
            return res.status(400).json({
                text: "❌ Format request tidak valid (Body kosong).",
                timestamp: Date.now()
            });
        }
        console.log(req.body);
        let _prompt = req?.body?.prompt;
        if (!_prompt && req?.body) {
            try {
                for (const key in req.body) {
                    const value = req.body[key];

                    if (typeof value === "string") {
                        _prompt = value;
                        break;
                    }
                }
            } catch (_) {}
        }
        const prompt = _prompt;
        console.log(prompt);
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

app.get("/health", (req, res) => {
    res.json({
        code: 200,
        timestamp: Date.now()
    });
});

// Rute GET Tambahan khusus untuk tes langsung di kolom URL Browser
app.get("/api/ai/tes", async (req, res) => {
    try {
        // Mengambil prompt dari query URL (?prompt=...)
        const prompt = req.query.prompt;

        if (!prompt) {
            return res.send(
                "Silakan masukkan prompt di URL. Contoh: http://127.0.0"
            );
        }

        // Menampilkan format JSON langsung di layar browser
        res.json({ text: "Response.text" });
    } catch (error) {
        res.status(500).send(error.message);
    }
});

// Kode tambahan untuk melacak URL yang masuk
app.use((req, res, next) => {
    console.log(`[Request Masuk] Method: ${req.method} | URL: ${req.url}`);
    next();
});

app.listen(port, () => {
    console.log(`Express berjalan di http://localhost:${port}`);
});

/**
app.listen(port, '0.0.0.0', () => {
  console.log(`Server AI berjalan dan siap menerima koneksi dari HP/Emulator!`);
});
*/
