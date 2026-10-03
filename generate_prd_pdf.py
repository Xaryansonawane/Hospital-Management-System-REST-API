import os
import subprocess
import sys
import re

def markdown_to_html(md_text):
    # Convert basic markdown elements to HTML
    lines = md_text.splitlines()
    html_lines = []
    in_code_block = False
    code_block_lang = ""
    code_buffer = []
    in_table = False
    table_buffer = []
    in_ul = False
    in_ol = False
    
    for line in lines:
        stripped = line.strip()
        
        # Code block handling
        if stripped.startswith("```"):
            if not in_code_block:
                in_code_block = True
                code_block_lang = stripped[3:].strip()
                code_buffer = []
                continue
            else:
                in_code_block = False
                escaped_code = "\n".join(code_buffer).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                html_lines.append(f'<pre><code class="language-{code_block_lang}">{escaped_code}</code></pre>')
                continue
                
        if in_code_block:
            code_buffer.append(line)
            continue
            
        # Table handling
        if "|" in line and (line.startswith("|") or stripped.startswith("|")):
            if not in_table:
                in_table = True
                table_buffer = [line]
            else:
                table_buffer.append(line)
            continue
        else:
            if in_table:
                in_table = False
                html_lines.append(render_table(table_buffer))
                table_buffer = []
                
        # Unordered list handling
        if stripped.startswith("- ") or stripped.startswith("* "):
            if in_ol:
                html_lines.append("</ol>")
                in_ol = False
            if not in_ul:
                html_lines.append("<ul>")
                in_ul = True
            content = line.strip()[2:]
            html_lines.append(f"<li>{format_inline(content)}</li>")
            continue
            
        # Ordered list handling
        ol_match = re.match(r"^(\d+)\.\s+(.*)", stripped)
        if ol_match:
            if in_ul:
                html_lines.append("</ul>")
                in_ul = False
            if not in_ol:
                html_lines.append("<ol>")
                in_ol = True
            content = ol_match.group(2)
            html_lines.append(f"<li>{format_inline(content)}</li>")
            continue
            
        # Close lists if line is not a list item
        if in_ul and not (stripped.startswith("- ") or stripped.startswith("* ")):
            html_lines.append("</ul>")
            in_ul = False
        if in_ol and not re.match(r"^(\d+)\.\s+", stripped):
            html_lines.append("</ol>")
            in_ol = False
            
        # Blockquote / Alert handling
        if stripped.startswith("> [!NOTE]"):
            html_lines.append('<div class="alert alert-note"><strong>Note:</strong>')
            continue
        elif stripped.startswith("> [!IMPORTANT]"):
            html_lines.append('<div class="alert alert-important"><strong>Important:</strong>')
            continue
        elif stripped.startswith(">"):
            content = stripped[1:].strip()
            if content.endswith("</div>"):
                html_lines.append(f"<p>{format_inline(content)}</p></div>")
            else:
                html_lines.append(f"<p>{format_inline(content)}</p>")
            continue
            
        # Headings
        if stripped.startswith("### "):
            html_lines.append(f"<h3>{format_inline(stripped[4:])}</h3>")
        elif stripped.startswith("## "):
            html_lines.append(f"<h2>{format_inline(stripped[3:])}</h2>")
        elif stripped.startswith("# "):
            html_lines.append(f"<h1>{format_inline(stripped[2:])}</h1>")
        elif stripped == "---":
            html_lines.append('<hr class="divider" />')
        elif stripped == "":
            html_lines.append("")
        else:
            html_lines.append(f"<p>{format_inline(stripped)}</p>")
            
    if in_table:
        html_lines.append(render_table(table_buffer))
    if in_ul:
        html_lines.append("</ul>")
    if in_ol:
        html_lines.append("</ol>")
        
    return "\n".join(html_lines)

def render_table(rows):
    if len(rows) < 2:
        return ""
    html = ['<table class="table">']
    header_cols = [c.strip() for c in rows[0].split("|")[1:-1]]
    html.append("<thead><tr>")
    for col in header_cols:
        html.append(f"<th>{format_inline(col)}</th>")
    html.append("</tr></thead><tbody>")
    
    for row in rows[2:]: # skip separator row
        cols = [c.strip() for c in row.split("|")[1:-1]]
        html.append("<tr>")
        for col in cols:
            html.append(f"<td>{format_inline(col)}</td>")
        html.append("</tr>")
        
    html.append("</tbody></table>")
    return "\n".join(html)

def format_inline(text):
    # Badges
    text = re.sub(r"`(GET|POST|PUT|DELETE|PATCH)`", r'<span class="badge badge-\1">\1</span>', text, flags=re.IGNORECASE)
    # Role badges
    text = re.sub(r"`(ROLE_ADMIN|ADMIN)`", r'<span class="role-badge role-admin">ADMIN</span>', text)
    text = re.sub(r"`(ROLE_DOCTOR|DOCTOR)`", r'<span class="role-badge role-doctor">DOCTOR</span>', text)
    text = re.sub(r"`(ROLE_PATIENT|PATIENT)`", r'<span class="role-badge role-patient">PATIENT</span>', text)
    # Inline code
    text = re.sub(r"`([^`]+)`", r'<code>\1</code>', text)
    # Bold
    text = re.sub(r"\*\*([^*]+)\*\*", r'<strong>\1</strong>', text)
    # Italic
    text = re.sub(r"\*([^*]+)\*", r'<em>\1</em>', text)
    # Arrows
    text = text.replace("&rarr;", "&rarr;").replace("->", "&rarr;")
    return text

def build_full_html(body_content):
    return f"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Product Requirements Document (PRD) - Hospital Management System REST API</title>
<style>
    @page {{
        size: A4;
        margin: 18mm 15mm 18mm 15mm;
        @bottom-right {{
            content: "Page " counter(page);
            font-size: 8.5pt;
            color: #64748b;
        }}
        @bottom-left {{
            content: "Hospital Management System REST API - PRD";
            font-size: 8.5pt;
            color: #94a3b8;
        }}
    }}
    * {{
        box-sizing: border-box;
    }}
    body {{
        font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, Roboto, Helvetica, Arial, sans-serif;
        color: #1e293b;
        line-height: 1.55;
        font-size: 9.5pt;
        background-color: #ffffff;
        margin: 0;
        padding: 0;
    }}
    .cover-page {{
        page-break-after: always;
        height: 88vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        text-align: center;
        padding: 40px 20px;
    }}
    .cover-badge {{
        display: inline-block;
        background: #e0f2fe;
        color: #0369a1;
        font-size: 11pt;
        font-weight: 700;
        padding: 6px 18px;
        border-radius: 20px;
        text-transform: uppercase;
        letter-spacing: 1px;
        margin-bottom: 24px;
        border: 1px solid #bae6fd;
    }}
    .cover-title {{
        font-size: 30pt;
        font-weight: 800;
        color: #0f172a;
        margin: 0 0 16px 0;
        line-height: 1.2;
    }}
    .cover-subtitle {{
        font-size: 14pt;
        color: #475569;
        margin: 0 0 36px 0;
        max-width: 650px;
    }}
    .cover-divider {{
        width: 80px;
        height: 4px;
        background: #0284c7;
        margin: 0 auto 36px auto;
        border-radius: 2px;
    }}
    .cover-meta {{
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 16px;
        max-width: 600px;
        width: 100%;
        text-align: left;
        margin-top: 30px;
        background: #f8fafc;
        padding: 24px;
        border-radius: 12px;
        border: 1px solid #e2e8f0;
    }}
    .meta-item {{
        font-size: 9pt;
    }}
    .meta-label {{
        font-weight: 700;
        color: #64748b;
        text-transform: uppercase;
        font-size: 7.5pt;
        margin-bottom: 4px;
    }}
    .meta-value {{
        color: #0f172a;
        font-weight: 600;
    }}
    
    h1, h2, h3, h4 {{
        color: #0f172a;
        font-weight: 700;
        page-break-after: avoid;
    }}
    h1 {{
        font-size: 16pt;
        border-bottom: 2px solid #0284c7;
        padding-bottom: 8px;
        margin-top: 30px;
        margin-bottom: 14px;
        page-break-before: auto;
    }}
    h2 {{
        font-size: 12pt;
        color: #0369a1;
        margin-top: 22px;
        margin-bottom: 10px;
        border-left: 4px solid #0284c7;
        padding-left: 10px;
    }}
    h3 {{
        font-size: 10.5pt;
        margin-top: 16px;
        margin-bottom: 8px;
    }}
    p {{
        margin: 0 0 10px 0;
    }}
    .divider {{
        border: 0;
        height: 1px;
        background: #e2e8f0;
        margin: 24px 0;
    }}
    
    table {{
        width: 100%;
        border-collapse: collapse;
        margin: 12px 0 16px 0;
        font-size: 8.5pt;
        page-break-inside: avoid;
    }}
    th, td {{
        padding: 7px 10px;
        text-align: left;
        border: 1px solid #cbd5e1;
        vertical-align: top;
    }}
    th {{
        background-color: #f1f5f9;
        font-weight: 700;
        color: #334155;
    }}
    tr:nth-child(even) td {{
        background-color: #f8fafc;
    }}
    
    .badge {{
        display: inline-block;
        padding: 2px 6px;
        border-radius: 4px;
        font-size: 7.5pt;
        font-weight: 700;
        font-family: 'Consolas', monospace;
        text-transform: uppercase;
    }}
    .badge-get {{ background: #dbeafe; color: #1d4ed8; }}
    .badge-post {{ background: #dcfce7; color: #15803d; }}
    .badge-put {{ background: #fef3c7; color: #b45309; }}
    .badge-delete {{ background: #fee2e2; color: #b91c1c; }}
    
    .role-badge {{
        display: inline-block;
        padding: 1px 6px;
        border-radius: 3px;
        font-size: 7pt;
        font-weight: 700;
    }}
    .role-admin {{ background: #fee2e2; color: #991b1b; }}
    .role-doctor {{ background: #e0e7ff; color: #3730a3; }}
    .role-patient {{ background: #dcfce7; color: #166534; }}

    code {{
        font-family: 'Consolas', 'Courier New', monospace;
        background: #f1f5f9;
        color: #0f172a;
        padding: 2px 5px;
        border-radius: 4px;
        font-size: 8pt;
    }}
    pre {{
        background: #0f172a;
        color: #f8fafc;
        padding: 10px 12px;
        border-radius: 6px;
        font-family: 'Consolas', 'Courier New', monospace;
        font-size: 7.5pt;
        line-height: 1.4;
        overflow-x: auto;
        margin: 8px 0 12px 0;
        page-break-inside: avoid;
    }}
    pre code {{
        background: transparent;
        color: inherit;
        padding: 0;
    }}
    
    .alert {{
        border-left: 4px solid #0284c7;
        background: #f0f9ff;
        padding: 10px 14px;
        border-radius: 0 6px 6px 0;
        margin: 10px 0;
        font-size: 8.5pt;
        page-break-inside: avoid;
    }}
    ul, ol {{
        margin: 6px 0 12px 20px;
        padding: 0;
    }}
    li {{
        margin-bottom: 4px;
    }}
</style>
</head>
<body>

<!-- COVER PAGE -->
<div class="cover-page">
    <div class="cover-badge">Product Requirements Document (PRD)</div>
    <div class="cover-title">Hospital Management System REST API</div>
    <div class="cover-subtitle">Technical Specification, Domain Invariants & Implementation Guidelines for Engineering Teams</div>
    <div class="cover-divider"></div>
    
    <div class="cover-meta">
        <div class="meta-item">
            <div class="meta-label">Architecture Style</div>
            <div class="meta-value">Backend-Only REST API (Headless)</div>
        </div>
        <div class="meta-item">
            <div class="meta-label">Runtime / Framework</div>
            <div class="meta-value">Java 17 LTS / Spring Boot 3.3.4</div>
        </div>
        <div class="meta-item">
            <div class="meta-label">Server Port & Base URL</div>
            <div class="meta-value">8085 (http://localhost:8085/)</div>
        </div>
        <div class="meta-item">
            <div class="meta-label">Security & Authentication</div>
            <div class="meta-value">Stateless JWT / Bearer Token</div>
        </div>
        <div class="meta-item">
            <div class="meta-label">Primary Database</div>
            <div class="meta-value">MySQL 8.x (H2 In-Memory Fallback)</div>
        </div>
        <div class="meta-item">
            <div class="meta-label">Interactive Documentation</div>
            <div class="meta-value">Swagger UI (/swagger-ui/index.html)</div>
        </div>
    </div>
</div>

{body_content}

</body>
</html>
"""

def main():
    root_dir = os.path.dirname(os.path.abspath(__file__))
    prd_md_path = os.path.join(root_dir, "PRD.md")
    html_out_path = os.path.join(root_dir, "PRD.html")
    pdf_out_path = os.path.join(root_dir, "Hospital_Management_System_PRD.pdf")
    
    if not os.path.exists(prd_md_path):
        print(f"Error: {prd_md_path} not found.")
        sys.exit(1)
        
    print(f"Reading {prd_md_path}...")
    with open(prd_md_path, "r", encoding="utf-8") as f:
        md_text = f.read()
        
    print("Converting Markdown to styled HTML...")
    body_html = markdown_to_html(md_text)
    full_html = build_full_html(body_html)
    
    with open(html_out_path, "w", encoding="utf-8") as f:
        f.write(full_html)
        
    edge_paths = [
        r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
        r"C:\Program Files\Microsoft\Edge\Application\msedge.exe"
    ]
    edge_exe = None
    for p in edge_paths:
        if os.path.exists(p):
            edge_exe = p
            break
            
    if not edge_exe:
        print("Error: Microsoft Edge executable not found.")
        sys.exit(1)
        
    temp_dir = os.path.join(os.environ.get("TEMP", "C:\\temp"), "edge_prd_pdf")
    os.makedirs(temp_dir, exist_ok=True)
    
    print(f"Generating PRD PDF via {edge_exe}...")
    cmd = [
        edge_exe,
        "--headless",
        "--disable-gpu",
        "--no-sandbox",
        f"--user-data-dir={temp_dir}",
        f"--print-to-pdf={pdf_out_path}",
        html_out_path
    ]
    
    result = subprocess.run(cmd, capture_output=True, text=True)
    if os.path.exists(pdf_out_path) and os.path.getsize(pdf_out_path) > 0:
        size_kb = os.path.getsize(pdf_out_path) / 1024
        print(f"SUCCESS: PRD PDF generated at {pdf_out_path} ({size_kb:.1f} KB)")
    else:
        print(f"Failed to generate PDF. Exit code: {result.returncode}")
        print("Stderr:", result.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()
