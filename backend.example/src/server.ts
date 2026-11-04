import express, { Request, Response, NextFunction } from "express";

const app = express();
const port: number = 3000;

app.use(express.json());
app.use(express.text({ type: "*/*" }));

app.post("/api/ai", async (req: Request, res: Response) => {
    try {
        if (!req.body) {
            return res.status(400).json({
                text: "❌ Format request tidak valid (Body kosong).",
                timestamp: Date.now()
            });
        }
        console.log(req.body);
        
        let _prompt: string | undefined = req?.body?.prompt;
        
        if (!_prompt && req?.body && typeof req.body === "object") {
            try {
                for (const key in req.body as Record<string, unknown>) {
                    const value = (req.body as Record<string, unknown>)[key];

                    if (typeof value === "string") {
                        _prompt = value;
                        break;
                    }
                }
            } catch (_) {}
        }
        
        const prompt: string | undefined = _prompt;
        console.log(prompt);
        
        if (!prompt) {
            return res.status(400).json({
                code: 400,
                text: 'Harap masukkan kolom "prompt" di dalam body JSON.',
                timestamp: Date.now()
            });
        }

        const response: string =
            prompt === "Hi" ? "Halo juga coyy" : "Ini Adalah Respon ai";
            
        return res.json({
            code: 200,
            text: response,
            timestamp: Date.now()
        });
    } catch (error: any) {
        console.error("Error AI:", error);
        // Menambahkan 'return' di sini untuk memperbaiki error TS7030
        return res.status(500).json({
            code: 500,
            text: "Terjadi kesalahan pada server AI.",
            timestamp: Date.now()
        });
    }
});

// Mengubah req menjadi _req untuk memperbaiki error TS6133
app.get("/health", (_req: Request, res: Response) => {
    return res.json({
        code: 200,
        timestamp: Date.now()
    });
});

// Mengubah res menjadi _res untuk memperbaiki error TS6133
app.use((req: Request, _res: Response, next: NextFunction) => {
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
