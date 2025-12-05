require('dotenv').config({ override: true });
const express = require('express');
const mysql = require('mysql2');

const app = express();
app.use(express.json());

const pool = mysql.createPool({
  host: process.env.DB_HOST,
  port: Number(process.env.DB_PORT || 3306),
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

const table = process.env.TABLE_NAME || 'avioesdb';
const aeroportosTable = process.env.AEROPORTOS_TABLE || 'aeroportos';

app.get(`/api/${table}`, async (req, res) => {
  try {
    const [rows] = await pool.promise().query(`SELECT * FROM \`${table}\``);
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: 'Falha ao consultar o banco' });
  }
});

app.get('/api/v1/aeroportos', async (req, res) => {
  try {
    const [rows] = await pool.promise().query(`SELECT * FROM \`${aeroportosTable}\``);
    res.json(rows);
  } catch (err) {
    console.error('Erro ao listar aeroportos', err);
    res.status(500).json({ error: 'Falha ao consultar aeroportos' });
  }
});

app.get('/api/v1/aeroportos/:iata', async (req, res) => {
  try {
    const [rows] = await pool.promise().query(`SELECT * FROM \`${aeroportosTable}\` WHERE iata = ?`, [req.params.iata]);
    if (!rows || rows.length === 0) {
      res.status(404).json({ error: 'Aeroporto não encontrado' });
      return;
    }
    res.json(rows[0]);
  } catch (err) {
    console.error('Erro ao obter aeroporto por IATA', err);
    res.status(500).json({ error: 'Falha ao consultar aeroporto' });
  }
});

app.get('/api/v1/aeroportos/id/:id', async (req, res) => {
  try {
    const id = Number(req.params.id);
    if (!Number.isInteger(id) || id <= 0) {
      res.status(400).json({ error: 'id inválido' });
      return;
    }
    const [rows] = await pool.promise().query(`SELECT * FROM \`${aeroportosTable}\` WHERE id = ?`, [id]);
    if (!rows || rows.length === 0) {
      res.status(404).json({ error: 'Aeroporto não encontrado' });
      return;
    }
    res.json(rows[0]);
  } catch (err) {
    console.error('Erro ao obter aeroporto por id', err);
    res.status(500).json({ error: 'Falha ao consultar aeroporto' });
  }
});

app.post('/api/v1/aeroportos', async (req, res) => {
  try {
    const body = req.body || {};
    if (!body.iata) {
      res.status(400).json({ error: 'Campo iata é obrigatório' });
      return;
    }
    const keys = Object.keys(body);
    const placeholders = keys.map(() => '?').join(', ');
    const columns = keys.map(k => `\`${k}\``).join(', ');
    const values = keys.map(k => body[k]);
    const sql = `INSERT INTO \`${aeroportosTable}\` (${columns}) VALUES (${placeholders})`;
    await pool.promise().query(sql, values);
    res.status(201).json({ message: 'Aeroporto criado' });
  } catch (err) {
    if (err && err.code === 'ER_DUP_ENTRY') {
      res.status(409).json({ error: 'Aeroporto já existe' });
      return;
    }
    console.error('Erro ao criar aeroporto', err);
    res.status(500).json({ error: 'Falha ao criar aeroporto' });
  }
});

app.put('/api/v1/aeroportos/:iata', async (req, res) => {
  try {
    const body = req.body || {};
    const fields = Object.keys(body).filter(k => k !== 'iata');
    if (fields.length === 0) {
      res.status(400).json({ error: 'Nenhum campo para atualizar' });
      return;
    }
    const setClause = fields.map(f => `\`${f}\` = ?`).join(', ');
    const values = fields.map(f => body[f]);
    values.push(req.params.iata);
    const sql = `UPDATE \`${aeroportosTable}\` SET ${setClause} WHERE iata = ?`;
    const [result] = await pool.promise().query(sql, values);
    if (!result || result.affectedRows === 0) {
      res.status(404).json({ error: 'Aeroporto não encontrado' });
      return;
    }
    res.json({ message: 'Aeroporto atualizado' });
  } catch (err) {
    console.error('Erro ao atualizar aeroporto', err);
    res.status(500).json({ error: 'Falha ao atualizar aeroporto' });
  }
});

app.put('/api/v1/aeroportos/id/:id', async (req, res) => {
  try {
    const id = Number(req.params.id);
    if (!Number.isInteger(id) || id <= 0) {
      res.status(400).json({ error: 'id inválido' });
      return;
    }
    const body = req.body || {};
    const fields = Object.keys(body).filter(k => k !== 'id');
    if (fields.length === 0) {
      res.status(400).json({ error: 'Nenhum campo para atualizar' });
      return;
    }
    const setClause = fields.map(f => `\`${f}\` = ?`).join(', ');
    const values = fields.map(f => body[f]);
    values.push(id);
    const sql = `UPDATE \`${aeroportosTable}\` SET ${setClause} WHERE id = ?`;
    const [result] = await pool.promise().query(sql, values);
    if (!result || result.affectedRows === 0) {
      res.status(404).json({ error: 'Aeroporto não encontrado' });
      return;
    }
    res.json({ message: 'Aeroporto atualizado' });
  } catch (err) {
    console.error('Erro ao atualizar aeroporto por id', err);
    res.status(500).json({ error: 'Falha ao atualizar aeroporto' });
  }
});

app.delete('/api/v1/aeroportos/:iata', async (req, res) => {
  try {
    const [result] = await pool.promise().query(`DELETE FROM \`${aeroportosTable}\` WHERE iata = ?`, [req.params.iata]);
    if (!result || result.affectedRows === 0) {
      res.status(404).json({ error: 'Aeroporto não encontrado' });
      return;
    }
    res.status(204).send();
  } catch (err) {
    console.error('Erro ao excluir aeroporto', err);
    res.status(500).json({ error: 'Falha ao excluir aeroporto' });
  }
});

app.delete('/api/v1/aeroportos/id/:id', async (req, res) => {
  try {
    const id = Number(req.params.id);
    if (!Number.isInteger(id) || id <= 0) {
      res.status(400).json({ error: 'id inválido' });
      return;
    }
    const [result] = await pool.promise().query(`DELETE FROM \`${aeroportosTable}\` WHERE id = ?`, [id]);
    if (!result || result.affectedRows === 0) {
      res.status(404).json({ error: 'Aeroporto não encontrado' });
      return;
    }
    res.status(204).send();
  } catch (err) {
    console.error('Erro ao excluir aeroporto por id', err);
    res.status(500).json({ error: 'Falha ao excluir aeroporto' });
  }
});

app.get('/health/db', async (req, res) => {
  try {
    const [rows] = await pool.promise().query('SELECT 1 AS ok');
    res.json({ ok: true });
  } catch (err) {
    console.error('Health DB error', err);
    res.status(500).json({ ok: false, code: err.code, message: err.message });
  }
});

app.get('/health/env', (req, res) => {
  res.json({
    host: process.env.DB_HOST,
    port: Number(process.env.DB_PORT || 3306),
    user: process.env.DB_USER,
    database: process.env.DB_NAME,
    aeroportosTable: process.env.AEROPORTOS_TABLE
  });
});

const port = Number(process.env.PORT || 3000);
app.listen(port, () => {
  console.log(`API rodando na porta ${port}`);
});