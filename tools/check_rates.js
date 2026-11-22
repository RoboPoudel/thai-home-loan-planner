// Validates site/rates.json (the bank rates every copy of the planner downloads). Usage: node tools/check_rates.js site/rates.json
const fs = require('fs');
const file = process.argv[2] || 'site/rates.json';
const j = JSON.parse(fs.readFileSync(file, 'utf8'));
const errs = [];
if (!/^\d{4}-\d{2}-\d{2}$/.test(j.updated || '')) errs.push('updated must be YYYY-MM-DD');
if (!Array.isArray(j.banks) || j.banks.length < 3) errs.push('banks must list at least 3 banks');
(j.banks || []).forEach((b, i) => {
  const w = (m) => errs.push(`banks[${i}] ${b && b.n}: ${m}`);
  if (typeof b.n !== 'string' || !b.n) w('n (name) missing');
  if (!Array.isArray(b.y) || b.y.length !== 3 || !b.y.every((x) => typeof x === 'number' && x >= 0 && x < 0.25)) w('y must be 3 yearly rates as fractions, e.g. 0.0255');
  if (!(b.mrr === null || (typeof b.mrr === 'number' && b.mrr > 0 && b.mrr < 0.25))) w('mrr must be a fraction or null');
  if (typeof b.margin !== 'number' || Math.abs(b.margin) > 0.05) w('margin must be a fraction like -0.0125');
  if (typeof b.ltv !== 'number' || b.ltv <= 0 || b.ltv > 1.5) w('ltv must be like 1.00');
  if (typeof b.term !== 'number' || b.term < 5 || b.term > 50) w('term must be years');
  if (b.u && !/^https:\/\//.test(b.u)) w('u must be an https link');
});
if (errs.length) { console.error(errs.join('\n')); process.exit(1); }
console.log(`ok – ${j.banks.length} banks, updated ${j.updated}`);
