import React, { useEffect, useState } from 'react';

const TypingAnimation = ({ paragraphs, onWritingCompleted }) => {
  const [currentParagraphIndex, setCurrentParagraphIndex] = useState(0);
  const [currentText, setCurrentText] = useState('');
  const [currentIndex, setCurrentIndex] = useState(0);
  const [showCursor, setShowCursor] = useState(true);
  const [writingCompleted, setWritingCompleted] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      if (currentIndex < paragraphs[currentParagraphIndex].length) {
        setCurrentText(prevText => prevText + paragraphs[currentParagraphIndex][currentIndex]);
        setCurrentIndex(prevIndex => prevIndex + 1);
      } else {
        clearInterval(interval);
        if (currentParagraphIndex + 1 < paragraphs.length) {
          setTimeout(() => {
            setCurrentParagraphIndex(prevIndex => prevIndex + 1);
            setCurrentIndex(0);
            setCurrentText('');
            setShowCursor(true);
            setWritingCompleted(false);
          }, 100);
        } else {
          setWritingCompleted(true);
          setShowCursor(false);
          if (typeof onWritingCompleted === 'function') {
            onWritingCompleted();
          }
        }
      }
    }, 13);

    const cursorInterval = setInterval(() => {
      setShowCursor(prevShowCursor => !prevShowCursor);
    }, 500);

    return () => {
      clearInterval(interval);
      clearInterval(cursorInterval);
    };
  }, [currentIndex, paragraphs, currentParagraphIndex, onWritingCompleted]);

  const cursorStyle = showCursor && !writingCompleted ? { marginLeft: '2px', borderLeft: '1px solid white', animation: 'blink 1s infinite' } : {};

  const createTextWithLineBreaks = (text) => {
    return text.split('\n').map((str, index) => (
      <React.Fragment key={index}>
        {str}
        {index !== text.split('\n').length - 1 && <br />}
      </React.Fragment>
    ));
  };

  return (
    <div style={{ display: 'inline-block' }}>
      {paragraphs.slice(0, currentParagraphIndex).map((paragraph, index) => (
        <div key={index} style={{ marginBottom: '20px' }} dangerouslySetInnerHTML={{ __html: paragraph.replace(/\n/g, '<br />') }} />
      ))}
      <span>{createTextWithLineBreaks(currentText)}</span>
      <span style={cursorStyle} />
    </div>
  );
};

export default TypingAnimation;
