import "./Character.css";
import { useState, useEffect } from "react";
import { fetchQuestions, createQuestion, deleteQuestion } from "../api/examsApi";
import { toast } from "react-toastify";
import "./ExamCreation.css";

export default function ExamCreation({ user, currentExam, setAdminView, handleLoadExams }) {
    const [questionForm, setQuestionForm] = useState({
        text: "",
        points: 1,
        answers: [
            { text: "", correct: true },
            { text: "", correct: false },
            { text: "", correct: false },
            { text: "", correct: false }
        ]
    });

    const alphaNumericRegex = /^[A-Za-z0-9]+$/;

    const [questions, setQuestions] = useState([]);

    const loadQuestions = async () => {
        try {
            const data = await fetchQuestions(user?.token, currentExam.examId);
            setQuestions(data);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        if (currentExam) {
            loadQuestions();
        }
    }, [currentExam]);

    const handleQuestionChange = (e) => {
        setQuestionForm(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleAnswerChange = (index, value) => {
        setQuestionForm(prev => {
            const answers = [...prev.answers];

            answers[index].text = value;

            return {
                ...prev,
                answers
            };
        });
    };

    const handleCorrectAnswer = (index) => {
        setQuestionForm(prev => ({
            ...prev,

            answers: prev.answers.map((answer, i) => ({
                ...answer,
                correct: i === index
            }))
        }));
    };

    const handleDeleteQuestion = async (question) => {
        if (!window.confirm("Usunąć to pytanie?")) {
            return;
        }

        try {
            await deleteQuestion(user?.token, question.questionId);
            await loadQuestions();
        } catch (err) {
            console.error(err);
        }
    };

    const handleAddQuestion = async () => {
        if (!questionForm.text.trim()) {
            toast.error("Pytanie nie może być puste!");
            return;
        }
        
        if (!alphaNumericRegex.test(questionForm.text)) {
            toast.error("Pytanie powinno zawierać tylko litery lub cyfry.");
            return;
        }

        if (questionForm.answers.some(answer => !answer.text.trim())) {
            toast.error("Odpowiedź nie może być pusta!");
            return;
        }

        if (questionForm.answers.some(answer => !alphaNumericRegex.test(answer.text))) {
            toast.error("Odpowiedzi powinny zawierać tylko litery i cyfry!");
            return;
        }

        if(questionForm.points < 1){
            toast.error("Pytanie musi być warte co najmniej 1 punkt!");
            return;
        }

        try {

            await createQuestion(user?.token, {
                examId: currentExam.examId,
                text: questionForm.text,
                points: Number(questionForm.points),
                answers: questionForm.answers
            });

            setQuestionForm({
                text: "",
                points: 1,
                answers: [
                    { text: "", correct: true },
                    { text: "", correct: false },
                    { text: "", correct: false },
                    { text: "", correct: false }
                ]
            });

            await loadQuestions();

        } catch (err) {
            console.error(err);
        }
    };

    const handleSaveExam = () => {
        if(questions.length < 1){
            toast.error("Egzamin musi zawierać przynajmniej 1 pytanie!");
            return;
        }

        setAdminView("admin");
        handleLoadExams();
    }

    return (
        <div className="exam-editor">
            <h2 className="exam-title">
                Edytujesz: {currentExam.name}
            </h2>
            <div className="question-form">
                <label>Pytanie</label>
                <input
                    name="text"
                    placeholder="Pytanie"
                    value={questionForm.text}
                    onChange={handleQuestionChange}
                    className="input"
                />

                <label>Punkty</label>
                <input
                    name="points"
                    type="number"
                    min={1}
                    value={questionForm.points}
                    onChange={handleQuestionChange}
                    className="input small"
                />
            </div>

            <div className="answers-section">
                {questionForm.answers.map((answer, index) => (
                    <div key={index} className="answer-row">
                        <span className="answer-label">
                            {String.fromCharCode(65 + index)}.
                        </span>

                        <input
                            className="answer-input"
                            value={answer.text}
                            placeholder={`Odpowiedź ${String.fromCharCode(65 + index)}`}
                            onChange={(e) =>
                                handleAnswerChange(index, e.target.value)
                            }
                        />

                        <label className="correct-label">
                            Poprawne
                            <input
                                type="radio"
                                checked={answer.correct}
                                onChange={() => handleCorrectAnswer(index)}
                            />
                        </label>
                    </div>
                ))}
            </div>

            <button className="add-btn" onClick={handleAddQuestion}>
                Dodaj pytanie
            </button>

            <h3 className="section-title">Pytania</h3>

            <div className="questions-list">
                {questions.map((question, index) => (
                    <div className="question-item" key={question.id}>
                        <span>
                            {index + 1}. {question.text}
                        </span>

                        <button
                            className="delete-btn"
                            onClick={() => handleDeleteQuestion(question)}
                        >
                            Usuń
                        </button>
                    </div>
                ))}
            </div>

            <button className="save-btn" onClick={() => handleSaveExam()}>
                Zapisz
            </button>
        </div>
    )
};