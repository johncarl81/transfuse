package org.androidtransfuse.util.apache.commons;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;


public class WriterOutputStream extends OutputStream {

    private final Writer writer;
    private final CharsetDecoder decoder;
    private final boolean writeImmediately;
    private final ByteBuffer decoderIn;
    private final CharBuffer decoderOut;

    public WriterOutputStream(Writer writer, Charset charset) {
        this.decoderIn = ByteBuffer.allocate(128);
        this.writer = writer;
        this.decoder = charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?");
        this.writeImmediately = false;
        this.decoderOut = CharBuffer.allocate(1024);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int c = Math.min(len, this.decoderIn.remaining());
            this.decoderIn.put(b, off, c);
            this.processInput(false);
            len -= c;
            off += c;
        }

        if (this.writeImmediately) {
            this.flushOutput();
        }
    }

    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    public void write(int b) throws IOException {
        this.write(new byte[]{(byte) b}, 0, 1);
    }

    public void flush() throws IOException {
        this.flushOutput();
        this.writer.flush();
    }

    public void close() throws IOException {
        this.processInput(true);
        this.flushOutput();
        this.writer.close();
    }

    private void processInput(boolean endOfInput) throws IOException {
        this.decoderIn.flip();

        while (true) {
            CoderResult coderResult = this.decoder.decode(this.decoderIn, this.decoderOut, endOfInput);
            if (!coderResult.isOverflow()) {
                if (coderResult.isUnderflow()) {
                    this.decoderIn.compact();
                    return;
                } else {
                    throw new IOException("Unexpected coder result");
                }
            }

            this.flushOutput();
        }
    }

    private void flushOutput() throws IOException {
        if (this.decoderOut.position() > 0) {
            this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
            this.decoderOut.rewind();
        }
    }
}
